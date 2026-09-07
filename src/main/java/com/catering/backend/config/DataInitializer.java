package com.catering.backend.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.catering.backend.Caterer;
import com.catering.backend.CatererRepository;
import com.catering.backend.Location;
import com.catering.backend.LocationRepository;
import com.catering.backend.MenuItem;
import com.catering.backend.MenuItemRepository;
import com.catering.backend.User;
import com.catering.backend.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CatererRepository catererRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // Seed default guest user if no users exist
        if (userRepository.count() == 0) {
            User guest = new User("Guest Customer", "guest@catering.com", "9876543210", "Hyderabad, Telangana", "CUSTOMER");
            userRepository.save(guest);
            logger.info("Seeded default guest user.");
        }

        // Seed locations, caterers, and menu items if locations table is empty
        if (locationRepository.count() == 0) {
            logger.info("Initializing catering database with locations, caterers, and menu items...");

            // 1. Kukatpally
            Location kukatpally = locationRepository.save(new Location("Kukatpally", "kukatpally", "Hyderabad"));
            
            Caterer sriSai = catererRepository.save(new Caterer("Sri Sai Catering", "Specialist in traditional South Indian and vegetarian delicacies", "9848011111", kukatpally));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Veg Biryani", 120.0, "Main Course", "Fragrant basmati rice cooked with fresh garden vegetables and aromatic spices", sriSai),
                    new MenuItem("Paneer Butter Masala", 150.0, "Curry", "Rich cottage cheese cubes simmered in a creamy tomato-butter gravy", sriSai)
            ));

            Caterer spicyKitchen = catererRepository.save(new Caterer("Spicy Kitchen", "Famous for bold Andhra spices and flavorful rice bowls", "9848022222", kukatpally));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Chicken 65", 180.0, "Starters", "Crispy spiced fried chicken tossed with curry leaves and green chilies", spicyKitchen),
                    new MenuItem("Fried Rice", 100.0, "Main Course", "Wok-tossed aromatic rice with finely chopped vegetables and mild seasoning", spicyKitchen)
            ));

            // 2. KPHB
            Location kphb = locationRepository.save(new Location("KPHB", "kphb", "Hyderabad"));
            Caterer delightFoods = catererRepository.save(new Caterer("Delight Foods", "Specializing in North Indian favorites and sweet celebration treats", "9848033333", kphb));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Gulab Jamun", 50.0, "Dessert", "Golden brown milk dumplings soaked in cardamom infused sugar syrup", delightFoods),
                    new MenuItem("Chole Bhature", 130.0, "Main Course", "Spicy chickpea curry served with two hot fluffy bhaturas", delightFoods)
            ));

            // 3. Miyapur
            Location miyapur = locationRepository.save(new Location("Miyapur", "miyapur", "Hyderabad"));
            Caterer urbanTadka = catererRepository.save(new Caterer("Urban Tadka", "Modern street food catering with authentic Mumbai and Punjabi tadka", "9848044444", miyapur));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Pav Bhaji", 110.0, "Snacks", "Buttery spiced mashed vegetables served with soft toasted pavs", urbanTadka),
                    new MenuItem("Butter Naan", 40.0, "Breads", "Traditional tandoor-baked flatbread brushed with butter", urbanTadka)
            ));

            // 4. Balanagar
            Location balanagar = locationRepository.save(new Location("Balanagar", "balanagar", "Hyderabad"));
            Caterer ruchiCaterers = catererRepository.save(new Caterer("Ruchi Caterers", "Grand feast catering for weddings and family milestone celebrations", "9848055555", balanagar));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Mutton Biryani", 250.0, "Main Course", "Tender mutton pieces slow-cooked with basmati rice in dum style", ruchiCaterers),
                    new MenuItem("Veg Pulao", 100.0, "Main Course", "Mildly spiced fragrant rice cooked with fresh seasonal vegetables", ruchiCaterers)
            ));

            // 5. Ameerpet
            Location ameerpet = locationRepository.save(new Location("Ameerpet", "ameerpet", "Hyderabad"));
            Caterer classicCaterers = catererRepository.save(new Caterer("Classic Caterers", "Snack boxes, party platters and corporate appetizers", "9848066666", ameerpet));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Samosa", 20.0, "Starters", "Crisp triangular pastry stuffed with spiced potatoes and green peas", classicCaterers),
                    new MenuItem("Manchurian", 90.0, "Starters", "Crisp vegetable dumplings tossed in a savory Indo-Chinese garlic sauce", classicCaterers)
            ));

            // 6. SR Nagar
            Location srNagar = locationRepository.save(new Location("SR Nagar", "srnagar", "Hyderabad"));
            Caterer citySpices = catererRepository.save(new Caterer("City Spices", "Authentic tandoori grills and homestyle North & South curries", "9848077777", srNagar));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Tandoori Chicken", 200.0, "Starters", "Juicy bone-in chicken marinated in spiced yogurt and roasted in clay oven", citySpices),
                    new MenuItem("Mixed Veg Curry", 120.0, "Curry", "Seasonal vegetables simmered in a savory onion and tomato masala", citySpices)
            ));

            // 7. Madhapur
            Location madhapur = locationRepository.save(new Location("Madhapur", "madhapur", "Hyderabad"));
            Caterer flavorsOfIndia = catererRepository.save(new Caterer("Flavors of India", "Premium IT corridor corporate lunches and executive catering", "9848088888", madhapur));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Chicken Curry", 190.0, "Curry", "Rich tender chicken simmered in traditional spiced curry sauce", flavorsOfIndia),
                    new MenuItem("Jeera Rice", 80.0, "Main Course", "Aromatic long-grain basmati rice tempered with roasted cumin seeds and ghee", flavorsOfIndia)
            ));

            // 8. Mehdipatnam
            Location mehdipatnam = locationRepository.save(new Location("Mehdipatnam", "mehdipatnam", "Hyderabad"));
            Caterer hyderabadiDelights = catererRepository.save(new Caterer("Hyderabadi Delights", "Heritage Nizami recipes and world-renowned Hyderabadi biryani", "9848099999", mehdipatnam));
            menuItemRepository.saveAll(Arrays.asList(
                    new MenuItem("Double Ka Meetha", 70.0, "Dessert", "Royal Hyderabadi bread pudding garnished with saffron and dry fruits", hyderabadiDelights),
                    new MenuItem("Hyderabadi Biryani", 230.0, "Main Course", "Authentic spiced dum biryani layered with fragrant saffron rice", hyderabadiDelights)
            ));

            logger.info("Successfully seeded 8 locations, 9 caterers, and 16 menu items.");
        }
    }
}
