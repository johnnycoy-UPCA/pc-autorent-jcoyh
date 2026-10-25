package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.exception;

import co.edu.clase.pc_autorent_jcoyh.domain.exception.AutoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.AlquilerException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.ClienteException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.NoEncontradoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND - Cuando Auto, Cliente o Alquiler no existe en H2
    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(NoEncontradoException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage()
        );
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    // 409 CONFLICT - Excepciones específicas de (Auto)
    @ExceptionHandler(AutoException.class)
    public ResponseEntity<ErrorRespuesta> manejarAuto(AutoException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
    }

    // 400 BAD REQUEST - Datos Inválidos en alquileres (Días <= 0, cruces de fechas, etc.)
    @ExceptionHandler(AlquilerException.class)
    public ResponseEntity<ErrorRespuesta> manejarAlquiler(AlquilerException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    // 400 BAD REQUEST - Duplicidad o Datos Erróneos de Clientes
    @ExceptionHandler(ClienteException.class)
    public ResponseEntity<ErrorRespuesta> manejarCliente(ClienteException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

     // 500 INTERNAL SERVER ERROR - Otros errores del sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarExcepcionGeneral(Exception ex) {
        ErrorRespuesta error = new ErrorRespuesta(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Error interno en el servidor AutoRent: " + ex.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }

}
