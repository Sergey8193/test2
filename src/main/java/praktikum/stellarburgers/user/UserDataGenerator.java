package praktikum.stellarburgers.user;

import com.github.javafaker.Faker;

public class UserDataGenerator {
    private static final Faker FAKER = new Faker();
    private static final int MIN_PASSWORD_COUNT = 8;
    private static final int MAX_PASSWORD_COUNT = 12;

    public static UserRegistrationData getRandomUserRegistrationData() {
        String email = getRandomEmail();
        String password = getRandomPassword();
        String name = getRandomName();
        return new UserRegistrationData(email, password, name);
    }

    public static String getRandomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password(MIN_PASSWORD_COUNT, MAX_PASSWORD_COUNT);
    }

    public static String getRandomName() {
        return FAKER.name().username();
    }
}
