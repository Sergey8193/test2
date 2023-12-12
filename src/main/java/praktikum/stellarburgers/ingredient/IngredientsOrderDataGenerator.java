package praktikum.stellarburgers.ingredient;

import com.github.javafaker.Faker;
import praktikum.stellarburgers.user.UserClient;
import praktikum.stellarburgers.user.UserRegistrationData;
import praktikum.stellarburgers.user.UserSuccessInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static praktikum.stellarburgers.user.UserCredentials.getCredentialsFrom;
import static praktikum.stellarburgers.user.UserDataGenerator.getRandomUserRegistrationData;

public class IngredientsOrderDataGenerator {

    public static Integer generateInt(int from, int to ) {
        return from + (int) ( Math.random() * (to - from + 1));
    }

    public static List<String> getRandomIngredientIdListFromExistentData() {
        List<String> ingredientsIdList = new ArrayList<>();

        IngredientClient ingredientClient = new IngredientClient();
        UserClient userClient = new UserClient();
        UserRegistrationData userRegistrationData = getRandomUserRegistrationData();

        userClient.createUser(userRegistrationData);
        UserSuccessInfo userSuccessInfo = userClient
                .loginUser(getCredentialsFrom(userRegistrationData))
                .extract()
                .body()
                .as(UserSuccessInfo.class);

        IngredientsSuccessInfo ingredientsSuccessInfo = ingredientClient
                .getIngredients(userSuccessInfo.getAccessToken())
                .extract()
                .body()
                .as(IngredientsSuccessInfo.class);

        List <IngredientData> ingredientsData = ingredientsSuccessInfo.getData();

        List <IngredientData> bunIngredientsData = new ArrayList<>();
        ingredientsData.stream()
                .filter(ingredientData -> Objects.equals(ingredientData.getType(), "bun"))
                .forEach(bunIngredientsData::add);

        List <IngredientData> mainIngredientsData = new ArrayList<>();
        ingredientsData.stream()
                .filter(ingredientData -> Objects.equals(ingredientData.getType(), "main"))
                .forEach(mainIngredientsData::add);

        final int MAX_BUN_COUNT =  bunIngredientsData.size();
        final int MAX_MAIN_COUNT_LIMIT = 3;
        final int MAIN_INGREDIENTS_LIST_SIZE = mainIngredientsData.size();
        final int MAX_MAIN_COUNT =  Math.min(MAIN_INGREDIENTS_LIST_SIZE, MAX_MAIN_COUNT_LIMIT);
        final int BUN_INDEX = generateInt(0, MAX_BUN_COUNT - 1);
        final int MAIN_COUNT = generateInt(1, MAX_MAIN_COUNT);

        ingredientsIdList.add(bunIngredientsData.get(BUN_INDEX).get_id());
        ingredientsIdList.add(bunIngredientsData.get(BUN_INDEX).get_id());
        for(int i = 0; i < MAIN_COUNT; i++ ) {
            final int MAIN_INDEX = generateInt(0, MAIN_INGREDIENTS_LIST_SIZE - 1);
            ingredientsIdList.add(mainIngredientsData.get(MAIN_INDEX).get_id());
        }

        if (!Objects.equals(userSuccessInfo.getAccessToken(), null) &&
                (!userSuccessInfo.getAccessToken().isEmpty())) {
            userClient.logoutUser(userSuccessInfo.getRefreshToken());
            userClient.deleteUser(userSuccessInfo.getAccessToken());
        }

        return ingredientsIdList;
    }

    public static List<String> getRandomIngredientIdList() {
        List<String> ingredientsIdList = new ArrayList<>();

        final int MAX_LIST_COUNT =  3;
        final int LIST_COUNT = generateInt(1, MAX_LIST_COUNT);

        Faker faker = new Faker();

        ingredientsIdList.add(faker.random().hex(24));
        ingredientsIdList.add(faker.random().hex(24));

        for(int i = 0; i < LIST_COUNT; i++ ) {
            ingredientsIdList.add(
                    faker.random().hex(24)
            );
        }

        return ingredientsIdList;
    }
}
