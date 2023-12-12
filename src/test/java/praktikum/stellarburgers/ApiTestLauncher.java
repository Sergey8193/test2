package praktikum.stellarburgers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import praktikum.stellarburgers.ingredient.GetIngredientsTest;
import praktikum.stellarburgers.order.CreateOrderParametersTest;
import praktikum.stellarburgers.order.CreateOrderTest;
import praktikum.stellarburgers.order.GetOrdersTest;
import praktikum.stellarburgers.user.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateUserParametersTest.class,
        CreateUserTest.class,
        LoginUserParametersTest.class,
        LoginUserTest.class,
        ModifyUserParametersTest.class,
        ModifyUserTest.class,
        CreateOrderParametersTest.class,
        CreateOrderTest.class,
        GetOrdersTest.class,
        GetIngredientsTest.class
})
public class ApiTestLauncher {
}
