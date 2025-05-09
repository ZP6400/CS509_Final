package model.account;

import model.user.Customer;
import model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountInfoTest {

    @Test
    public void test_accountInfo_constructor_and_getters() {

        Account account = new Account(1, "Jane Doe", 7500, "Active");
        User user = new Customer("JD6200", "54321", account);

        AccountInfo account_info = new AccountInfo(account, user);

        assertEquals(account, account_info.account());
        assertEquals(user, account_info.user());
    }
}
