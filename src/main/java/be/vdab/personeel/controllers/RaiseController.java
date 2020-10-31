package be.vdab.personeel.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/raise")
@PreAuthorize("hasAnyAuthority(" +
        "'President', " +
        "'VP Marketing', " +
        "'VP Sales')")
class RaiseController {
}
