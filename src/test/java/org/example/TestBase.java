package org.example;

import org.example.api.ApiMethods;
import org.example.helpers.DataHelper;
import org.example.helpers.ResponseHelper;

public class TestBase {
    protected final ApiMethods apiMethods = new ApiMethods();
    protected final ResponseHelper responseHelper = new ResponseHelper();
    protected final DataHelper dataHelper = new DataHelper();
    protected final Checker checker = new Checker();
}
