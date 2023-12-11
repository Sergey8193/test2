package praktikum.stellarburgers.order;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.List;
import java.util.Locale;

import static praktikum.stellarburgers.ingredient.IngredientsOrderDataGenerator.getRandomIngredientIdList;
import static praktikum.stellarburgers.ingredient.IngredientsOrderDataGenerator.getRandomIngredientIdListFromExistentData;

public class OrderDataGenerator {

    public static OrderData getRandomOrderData(boolean existentData) {
        List<String> ingredientIdList = existentData
                ? getRandomIngredientIdListFromExistentData()
                : getRandomIngredientIdList();
        return new OrderData(ingredientIdList);
    }

    public static String getRandomAccessToken() {
        final int FIRST_PART_CONT = 36;
        final int SECOND_PART_CONT = 90;
        final int THIRD_PART_CONT = 43;

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String firstPart = fakeValuesService.regexify("[a-zA-Z1-9]{" + FIRST_PART_CONT + "}");
        String secondPart = fakeValuesService.regexify("[a-zA-Z1-9]{" + SECOND_PART_CONT + "}");
        String thirdPart = fakeValuesService.regexify("[a-zA-Z1-9_-]{" + THIRD_PART_CONT + "}");

        return "Bearer " + firstPart + "." + secondPart + "." + thirdPart;
    }

    public static String getRandomRefreshToken() {
        return new  Faker().random().hex(80);
    }
}
