package daoArchitecture.dao;
import daoArchitecture.entity.Product;


import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    private List<Product> products = new ArrayList<>();

    @Override
    public void save(Product product) {
        products.add(product);
    }



    @Override
//    public Product findById(int id) {
//
//        for (Product product : products) {
//
//            if (product.getId() == id) {
//                return product;
//            }
//        }
//
//        return null;
//    }
    public Product findById(int id) {

        return products.stream().filter((p)->p.getId()==id).findFirst().orElse(null);
    }

    @Override
    public void update(Product updatedProduct) {

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getId() == updatedProduct.getId()) {

                products.set(i, updatedProduct);
                return;
            }
        }

        System.out.println("Product not found");
    }

    @Override
    @Override
//    public void deleteById(int id) {
//
//        Iterator<Product> iterator = products.iterator();
//
//        boolean found = false;
//
//        while (iterator.hasNext()) {
//            Product p = iterator.next();
//
//            if (p.getId() == id) {
//                iterator.remove();
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            System.out.println("Product not found");
//        }
//    }
//    public void deleteById(int id) {
//
//        Product product = findById(id);
//
//        if (product != null) {
//            products.remove(product);
//        } else {
//            System.out.println("Product not found");
//        }
//    }
    @Override
    public void deleteById(int id) {

       boolean val =products.removeIf((p)->p.getId()==id);
    }
        public void deleteById(int id) {
        products.stream().
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public void deleteAll() {
        products.clear();
    }

    @Override
//    public List<Product> findByCategory(String category) {
//
//        List<Product> result = new ArrayList<>();
//
//        for (Product product : products) {
//
//            if (product.getCategory().equalsIgnoreCase(category)) {
//                result.add(product);
//            }
//        }
//
//        return result;
//    }

    public List<Product> findByCategory(String category) {

       return products.stream().filter((p)->p.getCategory().equalsIgnoreCase(category)).toList();
    }

//    @Override
//    public List<Product> findByBrand(String brand) {
//
//        List<Product> result = new ArrayList<>();
//
//        for (Product product : products) {
//
//            if (product.getBrand().equalsIgnoreCase(brand)) {
//                result.add(product);
//            }
//        }
//
//        return result;
//    }
    public List<Product> findByBrand(String brand){
        return products.stream().filter((p)->p.getBrand().equalsIgnoreCase(brand)).toList();
    }
}
