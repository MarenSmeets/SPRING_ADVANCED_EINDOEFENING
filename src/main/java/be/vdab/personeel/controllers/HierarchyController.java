package be.vdab.personeel.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hierarchy")
@PreAuthorize("hasAnyAuthority(" +
        "'President', " +
        "'Sale Manager (EMEA)', " +
        "'Sales Manager (APAC)', " +
        "'Sales Manager (NA)', " +
        "'Sales Rep', " +
        "'VP Marketing', " +
        "'VP Sales')")
class HierarchyController {
}
