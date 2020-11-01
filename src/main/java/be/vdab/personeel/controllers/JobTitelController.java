package be.vdab.personeel.controllers;


import be.vdab.personeel.repositories.JobTitelRepository;
import be.vdab.personeel.repositories.WerknemerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("jobTitles")
public class JobTitelController {

    private final JobTitelRepository jobTitelRepository;
    private final WerknemerRepository werknemerRepository;

    public JobTitelController(JobTitelRepository jobTitelRepository, WerknemerRepository werknemerRepository) {
        this.jobTitelRepository = jobTitelRepository;
        this.werknemerRepository = werknemerRepository;
    }

    @GetMapping
    public ModelAndView findAll(){
        return new ModelAndView("jobTitles", "titels", jobTitelRepository.findAll());
    }

    @GetMapping("{id}")
    public ModelAndView findById(@PathVariable long id){
        var modelAndView = new ModelAndView("jobTitles");
        modelAndView.addObject( "titels", jobTitelRepository.findAll());
        var titel = jobTitelRepository.findById(id);
        if(titel.isEmpty()){
            modelAndView.addObject("titelNietGevonden", "Jobtitel niet gevonden");
        } else {
            modelAndView.addObject("titelById", titel.get());
            modelAndView.addObject("werknemersPerTitel", werknemerRepository.findByJobtitel_Id(id));
        }
        return modelAndView;
    }
}
