package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.ClientDTO;
import io.spring.billing.entities.Client;
import io.spring.billing.manager.AbstractManager;
import io.spring.billing.manager.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientControllerImpl extends AbstractController<Client, ClientDTO, Long> implements ClientController {

    @Autowired
    private ClientManager manager;

    public ClientControllerImpl() {
        super(Client.class, ClientDTO.class);
    }

    @Override
    protected ClientManager getManager() {
        return this.manager;
    }

    // @WTF
    public ResponseEntity<List<ClientDTO>> findAll() {
        return new ResponseEntity<>(convertToDTOList(getManager().findAll()), HttpStatus.OK);
    }

}
