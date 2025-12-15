package com.ecommerce.config;

import com.ecommerce.entities.Category;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.SubCategory;
import com.ecommerce.entities.User;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.SubCategoryRepository;
import com.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Always ensure categories exist
        if (categoryRepository.count() == 0) {
            initializeCategories();
        }
        
        // Initialize users only if no users exist
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        
        // Always create products if they don't exist (regardless of users)
        if (productRepository.count() == 0) {
            System.out.println("========================================");
            System.out.println("No products found. Creating 63 products with images...");
            System.out.println("========================================");
            initializeProducts();
            System.out.println("✅ Successfully created 63 products!");
            System.out.println("========================================");
            // Only approve seed data products (created during initialization)
            approveSeedDataProducts();
        } else {
            System.out.println("✅ Found " + productRepository.count() + " existing products");
            System.out.println("ℹ️  New products added by sellers will require admin approval");
        }
    }
    
    /**
     * Approve only seed data products (created during initialization)
     * This method is only called when initializing products for the first time
     */
    private void approveSeedDataProducts() {
        List<Product> products = productRepository.findAll();
        int approvedCount = 0;
        for (Product product : products) {
            // Only approve seed data products (those created during initialization)
            // New products from sellers will remain pending
            product.setIsActive(true);
            product.setIsApproved(true);
            approvedCount++;
        }
        if (approvedCount > 0) {
            productRepository.saveAll(products);
            System.out.println("✅ Approved " + approvedCount + " seed data products");
        }
    }

    private void initializeCategories() {
        // Create Categories
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic devices and gadgets");
        electronics.setIsActive(true);
        categoryRepository.save(electronics);

        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setDescription("Men's and Women's clothing");
        clothing.setIsActive(true);
        categoryRepository.save(clothing);

        Category books = new Category();
        books.setName("Books");
        books.setDescription("Books and literature");
        books.setIsActive(true);
        categoryRepository.save(books);

        Category home = new Category();
        home.setName("Home & Kitchen");
        home.setDescription("Home and kitchen appliances");
        home.setIsActive(true);
        categoryRepository.save(home);

        Category sports = new Category();
        sports.setName("Sports & Fitness");
        sports.setDescription("Sports equipment and fitness gear");
        sports.setIsActive(true);
        categoryRepository.save(sports);

        Category beauty = new Category();
        beauty.setName("Beauty & Personal Care");
        beauty.setDescription("Beauty products and personal care items");
        beauty.setIsActive(true);
        categoryRepository.save(beauty);

        // Create SubCategories
        SubCategory smartphones = new SubCategory();
        smartphones.setCategory(electronics);
        smartphones.setName("Smartphones");
        smartphones.setIsActive(true);
        subCategoryRepository.save(smartphones);

        SubCategory laptops = new SubCategory();
        laptops.setCategory(electronics);
        laptops.setName("Laptops");
        laptops.setIsActive(true);
        subCategoryRepository.save(laptops);

        SubCategory headphones = new SubCategory();
        headphones.setCategory(electronics);
        headphones.setName("Headphones");
        headphones.setIsActive(true);
        subCategoryRepository.save(headphones);

        SubCategory mensClothing = new SubCategory();
        mensClothing.setCategory(clothing);
        mensClothing.setName("Men's Clothing");
        mensClothing.setIsActive(true);
        subCategoryRepository.save(mensClothing);

        SubCategory womensClothing = new SubCategory();
        womensClothing.setCategory(clothing);
        womensClothing.setName("Women's Clothing");
        womensClothing.setIsActive(true);
        subCategoryRepository.save(womensClothing);

        SubCategory fiction = new SubCategory();
        fiction.setCategory(books);
        fiction.setName("Fiction");
        fiction.setIsActive(true);
        subCategoryRepository.save(fiction);

        SubCategory kitchenAppliances = new SubCategory();
        kitchenAppliances.setCategory(home);
        kitchenAppliances.setName("Kitchen Appliances");
        kitchenAppliances.setIsActive(true);
        subCategoryRepository.save(kitchenAppliances);
    }

    private void initializeUsers() {
        // Create Admin User
        User admin = new User();
        admin.setEmail("admin@ecommerce.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPhoneNumber("1234567890");
        admin.setRole(User.Role.ROLE_ADMIN);
        admin.setIsActive(true);
        admin.setIsBlocked(false);
        admin.setIsApproved(true);
        userRepository.save(admin);

        // Create 10 Sellers
        createSeller("seller1@ecommerce.com", "seller123", "John", "Tech", "TechStore", "GST001", "PAN001");
        createSeller("seller2@ecommerce.com", "seller123", "Jane", "Fashion", "FashionHub", "GST002", "PAN002");
        createSeller("seller3@ecommerce.com", "seller123", "Mike", "Electronics", "ElectroMart", "GST003", "PAN003");
        createSeller("seller4@ecommerce.com", "seller123", "Sarah", "Books", "BookWorld", "GST004", "PAN004");
        createSeller("seller5@ecommerce.com", "seller123", "David", "Home", "HomeEssentials", "GST005", "PAN005");
        createSeller("seller6@ecommerce.com", "seller123", "Emma", "Sports", "SportZone", "GST006", "PAN006");
        createSeller("seller7@ecommerce.com", "seller123", "Chris", "Beauty", "BeautyMart", "GST007", "PAN007");
        createSeller("seller8@ecommerce.com", "seller123", "Lisa", "Fashion", "StyleShop", "GST008", "PAN008");
        createSeller("seller9@ecommerce.com", "seller123", "Tom", "Electronics", "GadgetHub", "GST009", "PAN009");
        createSeller("seller10@ecommerce.com", "seller123", "Amy", "Home", "HomeDecor", "GST010", "PAN010");

        // Create 2 Customers
        createCustomer("customer1@ecommerce.com", "customer123", "Alice", "Customer");
        createCustomer("customer2@ecommerce.com", "customer123", "Bob", "Buyer");
    }
    
    private void initializeProducts() {
        // Get or create sellers (they might already exist)
        User seller1 = userRepository.findByEmail("seller1@ecommerce.com")
                .orElseGet(() -> createSeller("seller1@ecommerce.com", "seller123", "John", "Tech", "TechStore", "GST001", "PAN001"));
        User seller2 = userRepository.findByEmail("seller2@ecommerce.com")
                .orElseGet(() -> createSeller("seller2@ecommerce.com", "seller123", "Jane", "Fashion", "FashionHub", "GST002", "PAN002"));
        User seller3 = userRepository.findByEmail("seller3@ecommerce.com")
                .orElseGet(() -> createSeller("seller3@ecommerce.com", "seller123", "Mike", "Electronics", "ElectroMart", "GST003", "PAN003"));
        User seller4 = userRepository.findByEmail("seller4@ecommerce.com")
                .orElseGet(() -> createSeller("seller4@ecommerce.com", "seller123", "Sarah", "Books", "BookWorld", "GST004", "PAN004"));
        User seller5 = userRepository.findByEmail("seller5@ecommerce.com")
                .orElseGet(() -> createSeller("seller5@ecommerce.com", "seller123", "David", "Home", "HomeEssentials", "GST005", "PAN005"));
        User seller6 = userRepository.findByEmail("seller6@ecommerce.com")
                .orElseGet(() -> createSeller("seller6@ecommerce.com", "seller123", "Emma", "Sports", "SportZone", "GST006", "PAN006"));
        User seller7 = userRepository.findByEmail("seller7@ecommerce.com")
                .orElseGet(() -> createSeller("seller7@ecommerce.com", "seller123", "Chris", "Beauty", "BeautyMart", "GST007", "PAN007"));
        User seller8 = userRepository.findByEmail("seller8@ecommerce.com")
                .orElseGet(() -> createSeller("seller8@ecommerce.com", "seller123", "Lisa", "Fashion", "StyleShop", "GST008", "PAN008"));
        User seller9 = userRepository.findByEmail("seller9@ecommerce.com")
                .orElseGet(() -> createSeller("seller9@ecommerce.com", "seller123", "Tom", "Electronics", "GadgetHub", "GST009", "PAN009"));
        User seller10 = userRepository.findByEmail("seller10@ecommerce.com")
                .orElseGet(() -> createSeller("seller10@ecommerce.com", "seller123", "Amy", "Home", "HomeDecor", "GST010", "PAN010"));

        // Get categories (they should exist, but create if not)
        Category electronics = categoryRepository.findByName("Electronics")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Electronics");
                    cat.setDescription("Electronic devices and gadgets");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });
        Category clothing = categoryRepository.findByName("Clothing")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Clothing");
                    cat.setDescription("Men's and Women's clothing");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });
        Category books = categoryRepository.findByName("Books")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Books");
                    cat.setDescription("Books and literature");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });
        Category home = categoryRepository.findByName("Home & Kitchen")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Home & Kitchen");
                    cat.setDescription("Home and kitchen appliances");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });
        Category sports = categoryRepository.findByName("Sports & Fitness")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Sports & Fitness");
                    cat.setDescription("Sports equipment and fitness gear");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });
        Category beauty = categoryRepository.findByName("Beauty & Personal Care")
                .orElseGet(() -> {
                    Category cat = new Category();
                    cat.setName("Beauty & Personal Care");
                    cat.setDescription("Beauty products and personal care items");
                    cat.setIsActive(true);
                    return categoryRepository.save(cat);
                });

        // Get subcategories
        SubCategory smartphones = getSubCategory("Smartphones", electronics);
        SubCategory laptops = getSubCategory("Laptops", electronics);
        SubCategory headphones = getSubCategory("Headphones", electronics);
        SubCategory mensClothing = getSubCategory("Men's Clothing", clothing);
        SubCategory womensClothing = getSubCategory("Women's Clothing", clothing);
        SubCategory fiction = getSubCategory("Fiction", books);
        SubCategory kitchenAppliances = getSubCategory("Kitchen Appliances", home);

        // ========== ELECTRONICS - Smartphones (10 products) ==========
        createProduct("iPhone 15 Pro", "Latest iPhone with A17 Pro chip, 256GB", 
                new BigDecimal("129999"), new BigDecimal("64999"), seller1, electronics, smartphones, 
                50, "Apple", "Natural Titanium", null, 
                Arrays.asList("https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=500"));

        createProduct("Samsung Galaxy S24", "Flagship Android smartphone, 128GB", 
                new BigDecimal("79999"), new BigDecimal("39999"), seller1, electronics, smartphones, 
                40, "Samsung", "Phantom Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500"));

        createProduct("OnePlus 12", "Fast charging, 120Hz display, 256GB", 
                new BigDecimal("64999"), new BigDecimal("32499"), seller3, electronics, smartphones, 
                60, "OnePlus", "Silky Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=500"));

        createProduct("Google Pixel 8 Pro", "Best camera phone with AI features, 128GB", 
                new BigDecimal("89999"), new BigDecimal("44999"), seller3, electronics, smartphones, 
                35, "Google", "Obsidian", null, 
                Arrays.asList("https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=500"));

        createProduct("Xiaomi 14 Pro", "Flagship smartphone, Leica camera, 256GB", 
                new BigDecimal("69999"), new BigDecimal("34999"), seller9, electronics, smartphones, 
                45, "Xiaomi", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=500"));

        createProduct("Vivo X100 Pro", "Professional photography phone, 256GB", 
                new BigDecimal("74999"), new BigDecimal("37499"), seller9, electronics, smartphones, 
                30, "Vivo", "Blue", null, 
                Arrays.asList("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500"));

        createProduct("Realme GT 5 Pro", "Gaming smartphone, Snapdragon 8 Gen 3", 
                new BigDecimal("49999"), new BigDecimal("24999"), seller9, electronics, smartphones, 
                55, "Realme", "Orange", null, 
                Arrays.asList("https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=500"));

        createProduct("Nothing Phone 2", "Unique transparent design, 256GB", 
                new BigDecimal("44999"), new BigDecimal("22499"), seller3, electronics, smartphones, 
                40, "Nothing", "White", null, 
                Arrays.asList("https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=500"));

        createProduct("Motorola Edge 40", "Premium design, 128GB", 
                new BigDecimal("34999"), new BigDecimal("17499"), seller3, electronics, smartphones, 
                50, "Motorola", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500"));

        createProduct("Oppo Find X6 Pro", "Flagship camera phone, 256GB", 
                new BigDecimal("79999"), new BigDecimal("39999"), seller9, electronics, smartphones, 
                35, "Oppo", "Green", null, 
                Arrays.asList("https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=500"));

        // ========== ELECTRONICS - Laptops (8 products) ==========
        createProduct("MacBook Pro 16", "M3 Pro chip, 36GB RAM, 1TB SSD", 
                new BigDecimal("249999"), new BigDecimal("124999"), seller1, electronics, laptops, 
                20, "Apple", "Space Gray", "16 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500"));

        createProduct("Dell XPS 15", "4K OLED display, Intel i9, 32GB RAM", 
                new BigDecimal("189999"), new BigDecimal("94999"), seller1, electronics, laptops, 
                25, "Dell", "Platinum Silver", "15 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500"));

        createProduct("HP Spectre x360", "2-in-1 convertible laptop, Intel i7, 16GB RAM", 
                new BigDecimal("129999"), new BigDecimal("64999"), seller3, electronics, laptops, 
                30, "HP", "Nightfall Black", "14 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500"));

        createProduct("Lenovo ThinkPad X1", "Business laptop, lightweight, Intel i7", 
                new BigDecimal("139999"), new BigDecimal("69999"), seller3, electronics, laptops, 
                22, "Lenovo", "Black", "14 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500"));

        createProduct("ASUS ROG Strix", "Gaming laptop, RTX 4070, AMD Ryzen 9", 
                new BigDecimal("199999"), new BigDecimal("99999"), seller9, electronics, laptops, 
                15, "ASUS", "Black", "17 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500"));

        createProduct("Acer Predator Helios", "Gaming laptop, RTX 4060, Intel i7", 
                new BigDecimal("149999"), new BigDecimal("74999"), seller9, electronics, laptops, 
                18, "Acer", "Black", "16 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500"));

        createProduct("Microsoft Surface Laptop", "Premium Windows laptop, 13.5 inch", 
                new BigDecimal("119999"), new BigDecimal("59999"), seller1, electronics, laptops, 
                20, "Microsoft", "Platinum", "13.5 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500"));

        createProduct("MacBook Air M3", "Ultra-thin laptop, M3 chip, 512GB", 
                new BigDecimal("99999"), new BigDecimal("49999"), seller1, electronics, laptops, 
                35, "Apple", "Space Gray", "13 inch", 
                Arrays.asList("https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500"));

        // ========== ELECTRONICS - Headphones (5 products) ==========
        createProduct("Sony WH-1000XM5", "Premium noise-cancelling headphones", 
                new BigDecimal("29999"), new BigDecimal("14999"), seller1, electronics, headphones, 
                40, "Sony", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"));

        createProduct("Apple AirPods Pro", "Active noise cancellation, spatial audio", 
                new BigDecimal("24999"), new BigDecimal("12499"), seller1, electronics, headphones, 
                50, "Apple", "White", null, 
                Arrays.asList("https://images.unsplash.com/photo-1606220945770-b5b6c2c55bf1?w=500"));

        createProduct("Bose QuietComfort 45", "Premium noise-cancelling headphones", 
                new BigDecimal("27999"), new BigDecimal("13999"), seller3, electronics, headphones, 
                35, "Bose", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"));

        createProduct("JBL Tune 770NC", "Wireless noise-cancelling headphones", 
                new BigDecimal("8999"), new BigDecimal("4499"), seller9, electronics, headphones, 
                60, "JBL", "Blue", null, 
                Arrays.asList("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"));

        createProduct("Sennheiser Momentum 4", "Premium wireless headphones", 
                new BigDecimal("34999"), new BigDecimal("17499"), seller3, electronics, headphones, 
                25, "Sennheiser", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"));

        // ========== CLOTHING - Men's (8 products) ==========
        createProduct("Men's Casual T-Shirt Pack", "Pack of 3 cotton t-shirts", 
                new BigDecimal("1499"), new BigDecimal("749"), seller2, clothing, mensClothing, 
                100, "FashionHub", "Multi-color", "M", 
                Arrays.asList("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500"));

        createProduct("Men's Denim Jeans", "Classic fit denim jeans, stretchable", 
                new BigDecimal("2499"), new BigDecimal("1249"), seller2, clothing, mensClothing, 
                80, "FashionHub", "Blue", "32", 
                Arrays.asList("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"));

        createProduct("Men's Formal Shirt", "Premium cotton formal shirt", 
                new BigDecimal("1999"), new BigDecimal("999"), seller2, clothing, mensClothing, 
                60, "FashionHub", "White", "L", 
                Arrays.asList("https://images.unsplash.com/photo-1594938291221-94f313b0e69a?w=500"));

        createProduct("Men's Leather Jacket", "Genuine leather jacket, classic design", 
                new BigDecimal("8999"), new BigDecimal("4499"), seller8, clothing, mensClothing, 
                25, "StyleShop", "Black", "L", 
                Arrays.asList("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500"));

        createProduct("Men's Sports Shoes", "Comfortable sports shoes, breathable mesh", 
                new BigDecimal("3999"), new BigDecimal("1999"), seller2, clothing, mensClothing, 
                70, "FashionHub", "Black", "9", 
                Arrays.asList("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"));

        createProduct("Men's Winter Jacket", "Warm winter jacket, waterproof", 
                new BigDecimal("5999"), new BigDecimal("2999"), seller8, clothing, mensClothing, 
                40, "StyleShop", "Navy Blue", "L", 
                Arrays.asList("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500"));

        createProduct("Men's Polo Shirt", "Classic polo shirt, cotton blend", 
                new BigDecimal("1799"), new BigDecimal("899"), seller2, clothing, mensClothing, 
                65, "FashionHub", "Navy", "M", 
                Arrays.asList("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500"));

        createProduct("Men's Chinos", "Smart casual chinos, stretchable", 
                new BigDecimal("2299"), new BigDecimal("1149"), seller8, clothing, mensClothing, 
                55, "StyleShop", "Khaki", "32", 
                Arrays.asList("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"));

        // ========== CLOTHING - Women's (8 products) ==========
        createProduct("Women's Summer Dress", "Elegant summer dress, floral print", 
                new BigDecimal("2999"), new BigDecimal("1499"), seller2, clothing, womensClothing, 
                75, "FashionHub", "Floral", "M", 
                Arrays.asList("https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500"));

        createProduct("Women's Handbag", "Designer handbag, leather, multiple compartments", 
                new BigDecimal("4999"), new BigDecimal("2499"), seller2, clothing, womensClothing, 
                50, "FashionHub", "Brown", null, 
                Arrays.asList("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500"));

        createProduct("Women's Running Shoes", "Comfortable running shoes, breathable", 
                new BigDecimal("3999"), new BigDecimal("1999"), seller2, clothing, womensClothing, 
                90, "FashionHub", "Pink", "7", 
                Arrays.asList("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"));

        createProduct("Women's Winter Coat", "Warm winter coat, wool blend", 
                new BigDecimal("5999"), new BigDecimal("2999"), seller8, clothing, womensClothing, 
                40, "StyleShop", "Navy Blue", "M", 
                Arrays.asList("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=500"));

        createProduct("Women's Sunglasses", "UV protection, stylish frame", 
                new BigDecimal("1999"), new BigDecimal("999"), seller8, clothing, womensClothing, 
                60, "StyleShop", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=500"));

        createProduct("Women's Jeans", "Slim fit jeans, stretchable fabric", 
                new BigDecimal("2499"), new BigDecimal("1249"), seller2, clothing, womensClothing, 
                70, "FashionHub", "Blue", "28", 
                Arrays.asList("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500"));

        createProduct("Women's Blouse", "Elegant blouse, silk blend", 
                new BigDecimal("2299"), new BigDecimal("1149"), seller8, clothing, womensClothing, 
                55, "StyleShop", "White", "M", 
                Arrays.asList("https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500"));

        createProduct("Women's Heels", "Classic high heels, leather", 
                new BigDecimal("3499"), new BigDecimal("1749"), seller2, clothing, womensClothing, 
                45, "FashionHub", "Black", "7", 
                Arrays.asList("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500"));

        // ========== BOOKS (6 products) ==========
        createProduct("The Great Gatsby", "Classic American novel by F. Scott Fitzgerald", 
                new BigDecimal("499"), new BigDecimal("249"), seller4, books, fiction, 
                200, "Penguin", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        createProduct("To Kill a Mockingbird", "Harper Lee's masterpiece, Pulitzer Prize winner", 
                new BigDecimal("599"), new BigDecimal("299"), seller4, books, fiction, 
                150, "HarperCollins", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        createProduct("1984 by George Orwell", "Dystopian fiction classic", 
                new BigDecimal("449"), new BigDecimal("224"), seller4, books, fiction, 
                180, "Penguin", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        createProduct("The Alchemist", "Inspirational novel by Paulo Coelho", 
                new BigDecimal("399"), new BigDecimal("199"), seller4, books, fiction, 
                220, "HarperOne", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        createProduct("Pride and Prejudice", "Jane Austen's classic romance novel", 
                new BigDecimal("449"), new BigDecimal("224"), seller4, books, fiction, 
                160, "Penguin", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        createProduct("The Catcher in the Rye", "J.D. Salinger's classic coming-of-age novel", 
                new BigDecimal("499"), new BigDecimal("249"), seller4, books, fiction, 
                140, "Penguin", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500"));

        // ========== HOME & KITCHEN (8 products) ==========
        createProduct("Coffee Maker", "Automatic drip coffee maker, 12-cup capacity", 
                new BigDecimal("4999"), new BigDecimal("2499"), seller5, home, kitchenAppliances, 
                40, "KitchenAid", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1517668808823-f8f2c5b4b3c4?w=500"));

        createProduct("Air Fryer", "Healthy cooking, air frying technology, 5.5L", 
                new BigDecimal("8999"), new BigDecimal("4499"), seller5, home, kitchenAppliances, 
                35, "Philips", "Black", "5.5L", 
                Arrays.asList("https://images.unsplash.com/photo-1556912172-45b7abe8b7e1?w=500"));

        createProduct("Microwave Oven", "25L capacity, 10 power levels", 
                new BigDecimal("6999"), new BigDecimal("3499"), seller5, home, kitchenAppliances, 
                30, "LG", "Silver", "25L", 
                Arrays.asList("https://images.unsplash.com/photo-1574269909862-7e1d70bb8078?w=500"));

        createProduct("Blender", "High-speed blender, 1500W motor", 
                new BigDecimal("3999"), new BigDecimal("1999"), seller5, home, kitchenAppliances, 
                45, "Philips", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1573521193826-58c7dc2e13e3?w=500"));

        createProduct("Dining Table Set", "6-seater dining table with chairs, modern design", 
                new BigDecimal("29999"), new BigDecimal("14999"), seller10, home, null, 
                15, "HomeDecor", "Brown", null, 
                Arrays.asList("https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500"));

        createProduct("Sofa Set", "3+2+1 seater sofa set, comfortable cushions", 
                new BigDecimal("49999"), new BigDecimal("24999"), seller10, home, null, 
                10, "HomeDecor", "Gray", null, 
                Arrays.asList("https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=500"));

        createProduct("Bed Frame", "King size bed frame, wooden, with storage", 
                new BigDecimal("34999"), new BigDecimal("17499"), seller10, home, null, 
                12, "HomeDecor", "Brown", "King", 
                Arrays.asList("https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=500"));

        createProduct("Wardrobe", "4-door wardrobe, spacious, modern design", 
                new BigDecimal("39999"), new BigDecimal("19999"), seller10, home, null, 
                8, "HomeDecor", "White", null, 
                Arrays.asList("https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=500"));

        // ========== SPORTS & FITNESS (5 products) ==========
        createProduct("Yoga Mat", "Premium yoga mat, non-slip, 6mm thick", 
                new BigDecimal("1999"), new BigDecimal("999"), seller6, sports, null, 
                80, "FitnessPro", "Purple", null, 
                Arrays.asList("https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=500"));

        createProduct("Dumbbell Set", "Adjustable dumbbell set, 2x20kg", 
                new BigDecimal("4999"), new BigDecimal("2499"), seller6, sports, null, 
                25, "FitnessPro", "Black", "20kg", 
                Arrays.asList("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=500"));

        createProduct("Treadmill", "Electric treadmill, 3HP motor, 12 programs", 
                new BigDecimal("49999"), new BigDecimal("24999"), seller6, sports, null, 
                8, "FitnessPro", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1576678927484-cc907957088c?w=500"));

        createProduct("Resistance Bands Set", "Set of 5 resistance bands, various strengths", 
                new BigDecimal("1499"), new BigDecimal("749"), seller6, sports, null, 
                100, "FitnessPro", "Multi-color", null, 
                Arrays.asList("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=500"));

        createProduct("Exercise Bike", "Stationary exercise bike, adjustable resistance", 
                new BigDecimal("29999"), new BigDecimal("14999"), seller6, sports, null, 
                12, "FitnessPro", "Black", null, 
                Arrays.asList("https://images.unsplash.com/photo-1576678927484-cc907957088c?w=500"));

        // ========== BEAUTY & PERSONAL CARE (5 products) ==========
        createProduct("Skincare Set", "Complete skincare set, cleanser, toner, moisturizer", 
                new BigDecimal("2999"), new BigDecimal("1499"), seller7, beauty, null, 
                50, "BeautyMart", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=500"));

        createProduct("Hair Dryer", "Professional hair dryer, 2000W, ionic technology", 
                new BigDecimal("2999"), new BigDecimal("1499"), seller7, beauty, null, 
                40, "BeautyMart", "Pink", null, 
                Arrays.asList("https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=500"));

        createProduct("Electric Shaver", "Men's electric shaver, wet & dry, 3-head rotary", 
                new BigDecimal("4999"), new BigDecimal("2499"), seller7, beauty, null, 
                35, "BeautyMart", "Silver", null, 
                Arrays.asList("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"));

        createProduct("Makeup Kit", "Complete makeup kit, 20 shades, brushes included", 
                new BigDecimal("3999"), new BigDecimal("1999"), seller7, beauty, null, 
                45, "BeautyMart", "Multi-color", null, 
                Arrays.asList("https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=500"));

        createProduct("Perfume Set", "Luxury perfume set, 3 fragrances, 50ml each", 
                new BigDecimal("5999"), new BigDecimal("2999"), seller7, beauty, null, 
                30, "BeautyMart", null, null, 
                Arrays.asList("https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=500"));
    }

    private User createSeller(String email, String password, String firstName, String lastName, 
                              String businessName, String gst, String pan) {
        User seller = new User();
        seller.setEmail(email);
        seller.setPassword(passwordEncoder.encode(password));
        seller.setFirstName(firstName);
        seller.setLastName(lastName);
        seller.setPhoneNumber("9876543210");
        seller.setRole(User.Role.ROLE_SELLER);
        seller.setIsActive(true);
        seller.setIsBlocked(false);
        seller.setIsApproved(true);
        seller.setBusinessName(businessName);
        seller.setGstNumber(gst);
        seller.setPanNumber(pan);
        return userRepository.save(seller);
    }

    private User createCustomer(String email, String password, String firstName, String lastName) {
        User customer = new User();
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber("9876543210");
        customer.setRole(User.Role.ROLE_CUSTOMER);
        customer.setIsActive(true);
        customer.setIsBlocked(false);
        return userRepository.save(customer);
    }

    private SubCategory getSubCategory(String name, Category category) {
        return subCategoryRepository.findAll().stream()
                .filter(sc -> sc.getName().equals(name) && sc.getCategory().getId().equals(category.getId()))
                .findFirst()
                .orElse(null);
    }

    private void createProduct(String name, String description, BigDecimal price, BigDecimal discountPrice,
                              User seller, Category category, SubCategory subCategory, Integer stock,
                              String brand, String color, String size, List<String> images) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setSeller(seller);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setStockQuantity(stock);
        product.setBrand(brand);
        product.setColor(color);
        product.setSize(size);
        product.setImages(images);
        product.setIsActive(true);
        product.setIsApproved(true);
        product.setAverageRating(4.0 + Math.random() * 1.0); // Random rating 4.0-5.0
        product.setTotalReviews((int)(10 + Math.random() * 100)); // Random reviews 10-110
        productRepository.save(product);
    }
}
