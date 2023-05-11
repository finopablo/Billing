package com.globant.ibacademy.billing.controller;

import com.globant.ibacademy.billing.dto.BillDto;
import com.globant.ibacademy.billing.model.Bill;
import com.globant.ibacademy.billing.model.Product;
import com.globant.ibacademy.billing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bills")
@Tag(name = "Bill API ", description = "Bill management APIs")
public class BillController extends Controller<BillDto>
{

    final BillService billService;
    final ModelMapper mapper;
    public BillController(BillService billService, ModelMapper mapper) {
        this.billService = billService;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Retrieve a Bill by Id",
            description = "Get a Bill sending ID as a parameter",
            tags = { "bills", "get" })

    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> findBillById(@NotEmpty(message = "Invalid BillID Requested")  @PathVariable("id") Integer id) {
        return ResponseEntity.ok(toDto(billService.findById(id)));
    }


    @Operation(
            summary = "Retrieve All the Bills",
            tags = { "bills", "findAll" })
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Product.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) })
    @GetMapping("")
    public ResponseEntity<List<BillDto>> findAllBills() {
        return response.apply(billService.findAll().stream().map(this::toDto).toList());
    }

    @Operation(
            summary = "Retrieve a List of bills based customer",
            description = "Retrieve a List of bills based customer",
            tags = { "Bills","find" })

    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    @GetMapping(value = "", params = {"customer"})
    public ResponseEntity<List<BillDto>> findbyCustomer(@NotEmpty(message="customer name is required") @RequestParam String customer) {
        return response.apply(
                billService
                .findByCustomer(customer)
                .stream().map(this::toDto)
                .toList());
    }

    @Operation(
            summary = "Retrieve a List of bills based customer",
            description = "Retrieve a List of bills based customer",
            tags = { "Bills", "add" })
    @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    @PostMapping
    public ResponseEntity<BillDto> addNewBill(@Valid @RequestBody BillDto billDto) {

        Bill savedBill = billService.saveBill(fromDto(billDto));
        return ResponseEntity.created(location.apply(savedBill.getId())).body(toDto(savedBill));

    }

    BillDto toDto(Bill bill) {
            return mapper.map(bill, BillDto.class);
    }

    Bill fromDto(BillDto billDto) {
        return mapper.map(billDto, Bill.class);
    }
}
