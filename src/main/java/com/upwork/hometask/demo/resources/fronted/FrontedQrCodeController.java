package com.upwork.hometask.demo.resources.fronted;

import com.upwork.hometask.demo.resources.qrcode.model.CheckInInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityOutput;
import com.upwork.hometask.demo.resources.qrcode.model.ScheduleOutput;
import com.upwork.hometask.demo.services.qrCode.CheckInService;
import com.upwork.hometask.demo.services.qrCode.CurrentActivityService;
import com.upwork.hometask.demo.services.qrCode.DemoDataService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/*
This controller was written for demo purposes only and to be compatible with thymeleaf.
It has no function other than that. the real functionality is in the QrCodeController
 */

@Deprecated
@Controller
@Slf4j
@RequiredArgsConstructor
public class FrontedQrCodeController {
    private final CurrentActivityService currentActivityService;
    private final CheckInService checkInService;
    private final DemoDataService demoDataService;

    @RequestMapping("/")
    @ApiOperation(value = "List listSchedule")
    public String homePage(Model model) {
        model.addAttribute("appName", "UpworksDemo");
        ScheduleOutput data = demoDataService.getData();
        model.addAttribute("search", data);
        return "home";
    }

    @RequestMapping("/listCurrentActivity")
    @ApiOperation(value = "List Availability")
    public String listCurrentActivity(HttpServletRequest request, CurrentActivityInput input, Model model) {
        model.addAttribute("appName", "UpworksDemo");
        CurrentActivityOutput data = currentActivityService.listCurrentActivity(input);
        model.addAttribute("search", data);
        return "availability";
    }

    @ApiOperation(value = "Check In")
    @RequestMapping(value = "/checkIn")
    public String checkIn(HttpServletRequest request, CheckInInput input, Model model) {
        checkInService.checkIn(input);
        String result = "Check In successfully";
        model.addAttribute("result", result);
        return "checkResult";
    }
}
