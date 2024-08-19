package com.javalab.board.controller;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/blacklist")
public class BlacklistController {
    @Autowired
    private BlacklistService blacklistService;

    @GetMapping
    public String getBlacklist(Model model) {
        model.addAttribute("blacklist", blacklistService.getAllBlacklist());
        return "admin/blacklist";
    }

    @PostMapping("/toggle")
    @ResponseBody
    public Map<String, Object> toggleBlacklist(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        String type = payload.get("type");
        String reason = payload.get("reason");
        boolean isBlacklisted = blacklistService.toggleBlacklist(id, type, reason);
        Map<String, Object> response = new HashMap<>();
        response.put("isBlacklisted", isBlacklisted);
        return response;
    }

    @PostMapping("/updateReason")
    @ResponseBody
    public Map<String, Object> updateBlacklistReason(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        String type = payload.get("type");
        String reason = payload.get("reason");
        blacklistService.updateBlacklistReason(id, type, reason);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
}