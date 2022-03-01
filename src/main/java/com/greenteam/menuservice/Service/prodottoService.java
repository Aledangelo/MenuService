package com.greenteam.menuservice.Service;

import com.greenteam.menuservice.entity.Ordine;
import com.greenteam.menuservice.entity.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import com.greenteam.menuservice.repository.prodottoRepository;
import org.springframework.web.client.RestTemplate;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class prodottoService {

    @Autowired
    private prodottoRepository prodottoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Prodotto saveProdotto(Prodotto prodotto) throws Exception {
        return prodottoRepository.save(prodotto);
    }

    public String deleteProdottoById(String id) throws Exception{
        if(ObjectId.isValid(id)) {
            Prodotto prodotto = prodottoRepository.findById(id).orElseThrow();
            prodottoRepository.deleteById(id);
            return prodotto.getId();
        } else throw new Exception();
    }

    public Prodotto getProdottoById(String id) throws Exception{
        if(ObjectId.isValid(id))
            return prodottoRepository.findById(id).orElseThrow();
        else
            throw new Exception();
    }

    public List<Prodotto> getProdotti(){
        return prodottoRepository.findAll();
    }

    public Prodotto updateProdotto(Prodotto prodotto) throws Exception{
        Prodotto p = prodottoRepository.findById(prodotto.getId()).orElseThrow();
        prodottoRepository.deleteById(prodotto.getId());
        return prodottoRepository.save(prodotto);
    }

    public List<Prodotto> selezionaProdotti(List<String> prodotti, String indirizzo, String id_utente, String token) throws Exception{
        List<Prodotto> prod = new ArrayList<Prodotto>();
        for (String id : prodotti) {
            if(ObjectId.isValid(id))
                prod.add(prodottoRepository.findById(id).orElseThrow());
            else throw new Exception();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);
        HttpEntity<List<Prodotto>> req = new HttpEntity<List<Prodotto>>(prod, httpHeaders);

        restTemplate.exchange("http://10.16.11.227:9001/ordini/createOrder/" + id_utente + "/" + indirizzo, HttpMethod.POST, req, String.class);

        return prod;
    }

}
