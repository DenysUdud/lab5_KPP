import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

class ProductContainer implements Iterable<Product>, Serializable {
    private List<Product> products;

    public ProductContainer() {
        this.products = new LinkedList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> findValidProducts() {
        List<Product> validProducts = new LinkedList<>();
        for (Product product : products) {
            if (!product.isExpired()) {
                validProducts.add(product);
            }
        }
        return validProducts;
    }

    @Override
    public java.util.Iterator<Product> iterator() {
        return products.iterator();
    }

    @Override
    public String toString() {
        return "ProductContainer{" +
                "products=" + products +
                '}';
    }
}
