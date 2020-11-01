package be.vdab.personeel.controllers;


import be.vdab.personeel.forms.OpslagForm;
import be.vdab.personeel.repositories.WerknemerRepository;
import be.vdab.personeel.services.WerknemerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("raise")
//@PreAuthorize("hasAnyAuthority(" +
//        "'President', " +
//        "'VP Marketing', " +
//        "'VP Sales')")
@PreAuthorize("hasAnyAuthority('President')")
class RaiseController {

    private final WerknemerRepository repository;
    private final WerknemerService service;

    RaiseController(WerknemerRepository repository, WerknemerService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("raiseForm")
    public ModelAndView opslagForm(){
        return new ModelAndView("raise").addObject("OpslagForm", new OpslagForm(null));
    }


    @GetMapping("{id}")
    public ModelAndView werknemerById(@PathVariable long id){
        var modelAndView = new ModelAndView("raise");
        repository.findById(id).ifPresent(werknemer -> modelAndView.addObject("werknemer", werknemer));
        modelAndView.addObject("OpslagForm", new OpslagForm(null));
        return modelAndView ;
    }



    @PostMapping
    public String opslag(long id, @Valid OpslagForm form, Errors errors, RedirectAttributes redirect){
        if(errors.hasErrors()){
            redirect.addAttribute("foutBoodschap", "Het bedrag moet groter zijn dan nul");
            redirect.addAttribute("id", id);
            return "redirect:/raise/{id}";
        }
        var bedrag = form.getBedrag();
       service.opslagByNumber(id, bedrag);
       redirect.addAttribute("opslagSucces", true);
       redirect.addAttribute("id", id);
        return "redirect:/hierarchy/{id}" ;
    }


}
