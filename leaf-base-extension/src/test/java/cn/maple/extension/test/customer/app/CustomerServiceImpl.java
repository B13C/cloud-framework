package cn.maple.extension.test.customer.app;

import cn.maple.extension.test.customer.client.AddCustomerCmd;
import cn.maple.extension.test.customer.client.CustomerDto;
import cn.maple.extension.test.customer.client.CustomerService;
import cn.maple.extension.test.customer.client.GetOneCustomerQry;
import cn.maple.core.framework.util.GXResultUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private AddCustomerCmdExe addCustomerCmdExe;

    @Resource
    private GetOneCustomerQryExe getOneCustomerQryExe;

    @Override
    public GXResultUtils<String> addCustomer(AddCustomerCmd addCustomerCmd) {
        GXResultUtils<String> execute = addCustomerCmdExe.execute(addCustomerCmd);
        return execute;
    }

    @Override
    public GXResultUtils<CustomerDto> getCustomer(GetOneCustomerQry getOneCustomerQry) {
        return getOneCustomerQryExe.execute(getOneCustomerQry);
    }
}
