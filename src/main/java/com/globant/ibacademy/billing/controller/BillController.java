package com.globant.ibacademy.billing.controller;

import com.globant.ibacademy.billing.model.Bill;
import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/api/v1/bills")
@Tag(name = "Bill API ", description = "Bill management APIs")
public class BillController extends Controller<Bill>
{

    final BillService billService;

    Function<List<Product>, ResponseEntity<List<Product>>> bodyResponse = (list) ->  ( list.isEmpty()  ? ResponseEntity.notFound().build() : ResponseEntity.ok(list));
    Function<Integer, URI> location = (id) -> ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();


    public BillController(BillService billService) {
        this.billService = billService;
    }

    @Operation(
            summary = "Retrieve a Bill by Id",
            description = "Get a Bill sending ID as a parameter",
            tags = { "bills", "get" })
    @ApiResponses({
                    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Bill> findBillById(@NotEmpty(message = "Invalid BillID Requested")  @PathVariable("id") Integer id) {
        return ResponseEntity.ok(billService.findById(id));
    }


    @Operation(
            summary = "Retrieve All the Bills",
            tags = { "bills", "findAll" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("")
    public ResponseEntity<List<Bill>> findAllBills() {
        return response.apply(billService.findAll());
    }

    @Operation(
            summary = "Retrieve a List of bills based customer",
            description = "Retrieve a List of bills based customer",
            tags = { "Bills","find" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @GetMapping(value = "", params = {"customer"})
    public ResponseEntity<List<Bill>> findbyCustomer(@NotEmpty(message="customer name is required") @RequestParam String customer) {
        return response.apply(billService.findByCustomer(customer));
    }

    @Operation(
            summary = "Retrieve a List of bills based customer",
            description = "Retrieve a List of bills based customer",
            tags = { "Bills", "add" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity addNewBill(@RequestBody Bill bill) {
        Bill savedBill = billService.saveBill(bill);
        return ResponseEntity.created(location.apply(savedBill.id())).build();

    }
}
