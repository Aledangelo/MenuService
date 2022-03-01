package com.greenteam.menuservice.Controller;

import com.greenteam.menuservice.Service.prodottoService;
import com.greenteam.menuservice.entity.Ordine;
import com.greenteam.menuservice.entity.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/prodotto")
public class prodottoController {

    @Autowired
    private prodottoService prodottoService;

    private void logging(HttpServletRequest request) {
        log.info("IP: " + request.getRemoteAddr() + " USER: " + request.getRemoteUser() + " REQUEST URL: " + request.getRequestURL() + " METHOD: " + request.getMethod());
    }

    @GetMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token, HttpServletRequest request) throws ServletException {
        logging(request);
        request.logout();
        return "logout";
    }

    @GetMapping("/Dashboard")
    public String Dashboard(HttpServletRequest request, Authentication authentication, Model model) {
        logging(request);
        int r = 0;
        String role = authentication.getAuthorities().stream().findFirst().get().toString();
        if (role.equals("ROLE_admin_role")) {
            r = 2;
        } else if (role.equals("ROLE_rider_role")) {
            r = 1;
        }
        model.addAttribute("r", r);
        return "dashboard";

    }
    @GetMapping("/form_modifica/{id}")
    public String formModifica(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        logging(request);
        Prodotto prodotto = new Prodotto();
        prodotto.setId(id);
        model.addAttribute("prodotto", prodotto);
        return "form_modifica_prodotto";
    }

    @RolesAllowed("admin_role")
    @GetMapping("/form_upload")
    public String uploadForm(Model model) {
        model.addAttribute("prodotto", new Prodotto());
        return "uploadInterface";
    }

    @RolesAllowed("admin_role")
    @PostMapping("/upload")
    public String saveProdotto (@Valid @ModelAttribute Prodotto prodotto, HttpServletRequest request) throws Exception {
        logging(request);
        try {
            prodottoService.saveProdotto(prodotto);
            return "evento_creato";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }

    }

    @GetMapping("/get")
    @RolesAllowed({"admin_role", "user_role"})
    public String getProdotti(Model model, HttpServletRequest request, Authentication authentication) {
        logging(request);
        try {
            List<Prodotto> listaProdotti = prodottoService.getProdotti();
            model.addAttribute("prodotto", new Prodotto());
            model.addAttribute("listaProdotti", listaProdotti);
            String role = authentication.getAuthorities().stream().findFirst().get().toString();
            if (role.equals("ROLE_admin_role")) {
                return  "prodottiTOT";
            } else {
                return "Menu";
            }
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("admin_role")
    @GetMapping("/delete/{id}")
    public String deleteProdotto(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            logging(request);
            prodottoService.deleteProdottoById(id);
            return "ordineOk";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @PostMapping("/update/{id}")
    public String updateProdotto(@PathVariable("id") String id, @Valid @ModelAttribute Prodotto prodotto, HttpServletRequest request) {
        try {
            logging(request);
            prodotto.setId(id);
            prodottoService.updateProdotto(prodotto);
            return "ordineOK";
        } catch (Exception e){
            return "pagina_inaccessibile";
        }

    }

    @PostMapping("/ordina")
    public String ordinaProdotti(@RequestHeader("Authorization") String token, @RequestBody String order, HttpServletRequest request, Model model) {
        try {
            logging(request);
            List<String> prodotti = new ArrayList<>(List.of(order.split("&prodotti=")));
            prodotti.remove(0);
            List<String> tmp = List.of(prodotti.get(prodotti.size() - 1).split("&indirizzo="));
            prodotti.remove(prodotti.size() - 1);
            prodotti.add(tmp.get(0));
            String indirizzo = tmp.get(tmp.size() - 1);
            prodottoService.selezionaProdotti(prodotti, indirizzo, request.getRemoteUser(), token);
            return "ordineOk";
        } catch (Exception e){
            return "pagina_inaccessibile";
        }
    }
}
