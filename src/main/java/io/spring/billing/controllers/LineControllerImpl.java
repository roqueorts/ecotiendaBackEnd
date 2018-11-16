package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.LineDTO;
import io.spring.billing.entities.Line;
import io.spring.billing.manager.AbstractManager;
import io.spring.billing.manager.LineManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LineControllerImpl extends AbstractController<Line, LineDTO, Long> implements LineController {

    @Autowired
    private LineManager manager;

    public LineControllerImpl() {
        super(Line.class, LineDTO.class);
    }

    @Override
    protected LineManager getManager() {
        return this.manager;
    }

    // @WTF
    public ResponseEntity<List<LineDTO>> findAll() {
        return new ResponseEntity<>(convertToDTOList(getManager().findAll()), HttpStatus.OK);
    }

}
