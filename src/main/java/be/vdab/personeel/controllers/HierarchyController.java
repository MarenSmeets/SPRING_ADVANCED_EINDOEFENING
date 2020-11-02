package be.vdab.personeel.controllers;


import be.vdab.personeel.repositories.WerknemerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("hierarchy")
@PreAuthorize("hasAnyAuthority(" +
        "'President', " +
        "'Sale Manager (EMEA)', " +
        "'Sales Manager (APAC)', " +
        "'Sales Manager (NA)', " +
        "'Sales Rep', " +
        "'VP Marketing', " +
        "'VP Sales')")
class HierarchyController {

    private final WerknemerRepository werknemerRepository;

    HierarchyController(WerknemerRepository werknemerRepository){
        this.werknemerRepository = werknemerRepository;
    }

    // initially show the highest ranking employee (has no chef)
    @GetMapping
    public ModelAndView highestInHierarchy() {
        var modelAndView = new ModelAndView("hierarchy");
        werknemerRepository.findByChefIsNull().ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
        return modelAndView;
    }

    // show a specific employee
    // either from a link on page hierarchy, or from a link on page jobTitles
    @GetMapping("{id}")
    public ModelAndView werknemerById(@PathVariable long id){
        var modelAndView = new ModelAndView("hierarchy");
        werknemerRepository.findById(id).ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
        return modelAndView;
    }
}
