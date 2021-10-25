package org.example;

import org.example.api.methods.user.ApiUserMethods;
import org.example.helpers.DataHelper;
import org.example.helpers.ResponseHelper;

public class TestBase extends CoreTest {
    protected final ResponseHelper responseHelper = new ResponseHelper();
    protected final DataHelper dataHelper = new DataHelper();
    protected final Checker checker = new Checker();
    protected final ApiUserMethods apiUserMethods = new ApiUserMethods();
}
