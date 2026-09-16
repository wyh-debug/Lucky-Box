package com.wyh.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.luckybox.constant.RedisConstants;
import com.wyh.luckybox.mapper.*;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.Product;
import com.wyh.luckybox.pojo.entity.ProductSku;
import com.wyh.luckybox.pojo.vo.ProductDetailVO;
import com.wyh.luckybox.service.IProductService;
import com.wyh.luckybox.service.IProductSkuService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements IProductSkuService{
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SynthesisRecordMapper synthesisRecordMapper;

    @Resource
    private SynthesisRuleMapper synthesisRuleMapper;

    @Resource
    private MysteryBoxProductRelMapper mysteryBoxProductRelMapper;
    @Resource
    private ProductOrderMapper productOrderMapper;

    @Resource
    private IProductService productService;
    @Override
    public Result addProductSKu(ProductSku productSku) {
        boolean success = this.save(productSku);
        if(!success) {
            return Result.fail("保存失败");
        }
        stringRedisTemplate.delete(RedisConstants.PRODUCT_SKU_KEY  + productSku.getProductId());
        return Result.ok();
    }

    @Override
    public Result updateProductSku(ProductSku productSku) {
        boolean success = this.updateById(productSku);
        stringRedisTemplate.delete(RedisConstants.PRODUCT_SKU_KEY  + productSku.getProductId());
        return Result.ok();
    }

    @Override
    public Result deleteProductSku(Long id) {
        if(id == null) {
            return Result.fail("商品Sku不正确");
        }
        boolean flag1 = synthesisRecordMapper.existSynthesisRecord(id);
        boolean flag2 = synthesisRuleMapper.existSynthesisRule(id);
        boolean flag3 = mysteryBoxProductRelMapper.existMysteryBoxProductRel(id);
        boolean flag4 = productOrderMapper.existsProductOrder(id);
        if(flag1 || flag2 || flag3 || flag4) {
            return Result.fail("不可删除");
        }
        boolean success = this.removeById(id);
        if(!success) {
            return Result.fail("删除失败");
        }
        stringRedisTemplate.delete(RedisConstants.PRODUCT_SKU_KEY + id);

        return Result.ok();
    }

    /*
（根据productId进入缓存中取出product信息） ，第一次查询从数据库查，拼接 好规格返回给前端，默认第一个显示的sku是
     */
    @Override
    public Result selectByProductId(Long productId) throws InterruptedException {
        ProductDetailVO productDetailVO = this.buildProductDetail(productId, null);
        return Result.ok(productDetailVO);
    }

    @Override
    public Result selectByProductSkuId(Long productSkuId) throws InterruptedException {

        ProductSku sku = this.getById(productSkuId);
        if(Objects.isNull(sku)) {
            return Result.ok();
        }
        ProductDetailVO productDetailVO = this.buildProductDetail(sku.getProductId(), productSkuId);
        return Result.ok(productDetailVO);
    }

    private ProductDetailVO buildProductDetail(Long productId, Long defaultSkuId) throws InterruptedException {
        // 1. 查 Product（走缓存）
        Result r = productService.getProduct(productId);
        if (r.getData() == null) {
            return null;
        }
        Product product = (Product) r.getData();

        // 2. 查该商品下的所有 SKU（全量）
        List<ProductSku> skuList = this.list(
                new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId, productId)
        );
        if(skuList.isEmpty()) {
            return null;
        }

        if(defaultSkuId == null) {
            defaultSkuId = skuList.stream().min(Comparator.comparing(ProductSku::getPrice)).map(ProductSku::getId).orElse(skuList.get(0).getId());
        }
        // 3. 组装 VO（两个接口返回的结构完全一样）
        ProductDetailVO vo = new ProductDetailVO();
        vo.setProduct(product);
        vo.setSkuList(skuList);
        vo.setDefaultSkuId(defaultSkuId); // 只是这个值不同！
        return vo;
    }
}
