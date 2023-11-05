package com.upwork.hometask.demo.resources.fronted;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import com.upwork.hometask.demo.resources.iprule.model.CheckInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertSubnetInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchOutput;
import com.upwork.hometask.demo.services.iprule.IpRuleCheckService;
import com.upwork.hometask.demo.services.iprule.IpRuleService;
import com.upwork.hometask.demo.services.iprule.SubnetRuleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/*
This controller was written for demo purposes only and to be compatible with thymeleaf.
It has no function other than that. the real functionality is in the IpRuleController
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class FrontedController {

    private final IpRuleService ipRuleService;
    private final SubnetRuleService subnetRuleService;
    private final IpRuleCheckService ipRuleCheckService;

    @RequestMapping("/")
    @ApiOperation(value = "List With IP Filter Rule")
    public String homePage(Model model) {
        model.addAttribute("appName", "UpworksDemo");
        SearchOutput search = ipRuleService.search(new SearchInput());
        model.addAttribute("search", search);
        return "home";
    }

    @ApiOperation(value = "Delete With IP Filter Rule")
    @RequestMapping("/delete/{id}")
    public String delete(HttpServletRequest request, @PathVariable @Valid @Min(1) Long id, Model model) {
        ipRuleService.delete(id);
        model.addAttribute("appName", "UpworksDemo");
        model.addAttribute("search", ipRuleService.search(new SearchInput()));
        return "redirect:/";
    }

    @ApiOperation(value = "Get With IP Filter Rule Page")
    @RequestMapping("/create")
    public String create(HttpServletRequest request, Model model) {
        InsertInput insertInput = new InsertInput();
        model.addAttribute("insertInput", insertInput);
        return "create";
    }

    @ApiOperation(value = "Get With IP Subnet Filter Rule Page")
    @RequestMapping("/createSubnet")
    public String createSub(HttpServletRequest request, Model model) {
        InsertSubnetInput insertInput = new InsertSubnetInput();
        model.addAttribute("insertInput", insertInput);
        return "createSubnet";
    }

    @ApiOperation(value = "Get Check With IP filter Page")
    @RequestMapping("/check")
    public String check(HttpServletRequest request, Model model) {
        CheckInput insertInput = new CheckInput();
        model.addAttribute("insertInput", insertInput);
        return "check";
    }

    @ApiOperation(value = "Save With IP filter rule")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("insertInput") @Valid @RequestBody InsertInput input) {
        ipRuleService.create(input);
        return "redirect:/";
    }

    @ApiOperation(value = "Save With Ip Subnet Filter Rule")
    @RequestMapping(value = "/saveSubnet", method = RequestMethod.POST)
    public String saveSubnet(@ModelAttribute("insertInput") InsertSubnetInput input) {
        subnetRuleService.create(input);
        return "redirect:/";
    }


    @ApiOperation(value = "Check Pair Ips")
    @RequestMapping(value = "/checkPair", method = RequestMethod.POST)
    public String checkPair(@ModelAttribute("insertInput") CheckInput input, Model model) {
        Boolean aBoolean = ipRuleCheckService.executeFromDb(input.getSource(), input.getDestination());
        String result = "Source : "+input.getSource() + " ==>> "+" Destination : " + input.getDestination() + " :: Result = "+(aBoolean  ? " Allowed" : "Deny");
        model.addAttribute("result", result);
        return "checkResult";
    }

}