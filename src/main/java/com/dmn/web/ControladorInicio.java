package com.dmn.web;

import java.util.*;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.dmn.domain.Persona;
import com.dmn.servicio.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user) {
        List<Persona> personas = personaService.listarPersonas();
        log.info("ejecutando el controlador Spring MVC");
        log.info("usuario que hizo login:" + user);
        model.addAttribute("personas", personas);

        double saldoTotal = 0D;

        ArrayList<Float> saldos = new ArrayList<>();
        float saldoMax = 0;
        float saldoMin = 0;
        Persona personaSaldoMax = new Persona();
        Persona personaSaldoMin = new Persona();

        for (Persona p : personas) {
            saldoTotal += p.getSaldo();
            saldos.add(p.getSaldo());
        }

        Collections.sort(saldos);
        saldoMin = saldos.get(0);
        saldoMax = saldos.get(saldos.size() - 1);

        for (Persona p : personas) {
            if (p.getSaldo() == saldoMin) {
                personaSaldoMin = p;
            }
            if (p.getSaldo() == saldoMax) {
                personaSaldoMax = p;
            }
        }

        model.addAttribute("personaSaldoMinNombre", personaSaldoMin.getNombre());
        model.addAttribute("personaSaldoMinApellido", personaSaldoMin.getApellido());
        model.addAttribute("personaSaldoMaxNombre", personaSaldoMax.getNombre());
        model.addAttribute("personaSaldoMaxApellido", personaSaldoMax.getApellido());
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalClientes", personas.size());
        model.addAttribute("saldoMax", saldoMax);
        model.addAttribute("saldoMin", saldoMin);

        return "index";
    }

    @GetMapping("/agregar")
    public String agregar(Persona persona) {
        return "modificar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores) {
        if (errores.hasErrors()) {
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }

    @GetMapping("/detalle/{idPersona}")
    public String detalle(Persona persona, Model model) {
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona", persona);
        return "detalle";
    }

    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona, Model model) {
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }

    @GetMapping("/eliminar")
    public String eliminar(Persona persona) {
        personaService.eliminar(persona);
        return "redirect:/";
    }
}
