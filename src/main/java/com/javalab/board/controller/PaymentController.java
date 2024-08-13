package com.javalab.board.controller;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create/{jobPostId}")
    public String showCreatePaymentForm(@PathVariable("jobPostId") Long jobPostId, Model model) {
        // 채용공고 ID를 통해 공고 정보를 가져와야 함
        model.addAttribute("jobPostId", jobPostId);
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setJobPostId(jobPostId);
        model.addAttribute("paymentDto", paymentDto);
        return "createPayment";
    }

    @PostMapping("/create")
    public String createPayment(@ModelAttribute PaymentDto paymentDto, RedirectAttributes redirectAttributes) {
        paymentService.createPayment(paymentDto);
        redirectAttributes.addFlashAttribute("message", "결제가 생성되었습니다. 결제를 완료해 주세요.");
        return "redirect:/payment/confirm";
    }

    @GetMapping("/confirm")
    public String confirmPayment(Model model) {
        // 결제 확인 페이지 로직
        // 예를 들어, 사용자가 결제를 완료할 수 있는 페이지로 리디렉션
        return "confirmPayment";
    }

    @PostMapping("/complete")
    public String completePayment(@RequestParam("paymentId") Long paymentId, RedirectAttributes redirectAttributes) {
        PaymentDto paymentDto = paymentService.getPaymentById(paymentId);
        paymentService.updatePayment(paymentDto);
        redirectAttributes.addFlashAttribute("message", "결제가 완료되었습니다.");
        return "redirect:/jobPost/list";
    }
}
