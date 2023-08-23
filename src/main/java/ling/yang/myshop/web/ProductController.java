package ling.yang.myshop.web;

import ling.yang.myshop.Vo.ProductVo;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.exceptions.MyShopException;
import ling.yang.myshop.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;

@RestController
@RequestMapping(value = "/api/v1/product", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public List<ProductVo> listProduct() {
        return productService.list()
                             .stream()
                             .map(ProductVo::of)
                             .collect(Collectors.toList());
    }

    @GetMapping("/{productId}")
    public ProductVo oneProduct(@PathVariable int productId) {
        return productService.getOptById(productId)
                             .map(ProductVo::of)
                             .orElseThrow(() -> new MyShopException(PRODUCT_NOT_FOUND));
    }

    @PostMapping
    public ProductVo addProduct(@RequestBody ProductVo vo) {
        Product entity = Product.builder()
                                .name(vo.getName())
                                .price(vo.getPrice())
                                .amount(vo.getAmount())
                                .build();
        productService.save(entity);
        return this.oneProduct(entity.getId());
    }

    @PutMapping("/{productId}")
    public ProductVo updateProduct(@PathVariable int productId, @RequestBody ProductVo vo) {
        Optional<Product> optById = productService.getOptById(productId);
        if (optById.isEmpty()) {
            throw new MyShopException(PRODUCT_NOT_FOUND);
        }
        productService.updateById(Product.of(vo, productId));
        return this.oneProduct(productId);
    }

    @DeleteMapping("/{productId}")
    public boolean removeProduct(@PathVariable int productId) {
        Optional<Product> optById = productService.getOptById(productId);
        if (optById.isEmpty()) {
            throw new MyShopException(PRODUCT_NOT_FOUND);
        }
        return productService.removeById(productId);
    }
}
