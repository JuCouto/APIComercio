package com.residencia.comercio.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.comercio.dtos.ProdutoDTO;
import com.residencia.comercio.entities.Produto;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<List<Produto>> findAllProduto() {
		List<Produto> produtoList = produtoService.findAllProduto();
		if (produtoList.isEmpty())
			throw new NoSuchElementFoundException("Não foram encontrados produtos");
		else
			return new ResponseEntity<>(produtoService.findAllProduto(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findById(@PathVariable Integer id) {
		Produto produto = produtoService.findById(id);
		if (null == produto)
			throw new NoSuchElementFoundException("Não foi encontrado produto com o id ");
		else
			return new ResponseEntity<>(produto, HttpStatus.OK);
	}
	
	@GetMapping("/id")
	public ResponseEntity<Produto> findByIdRequest(@RequestParam @NotBlank Integer id) {
		Produto produto = produtoService.findById(id);
		if (null == produto)
			throw new NoSuchElementFoundException("Não foi encontrado produto com o id ");
		else
			return new ResponseEntity<>(produto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Produto> save(@Valid @RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.saveProduto(produto), HttpStatus.CREATED);
	}
	
	@PostMapping("/dto")
    public ResponseEntity<ProdutoDTO> saveDTO(@RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO novoProdutoDTO = produtoService.saveProdutoDTO(produtoDTO);
        return new ResponseEntity<>(novoProdutoDTO, HttpStatus.CREATED);
    }

	@PutMapping
	public ResponseEntity<Produto> update(@RequestBody Produto produto) {
		Produto produtoAtualizado = produtoService.updateProduto(produto);
		return new ResponseEntity<>(produtoAtualizado, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> updateComId(@PathVariable Integer id, @RequestBody Produto produto) {
		Produto produtoAtualizado = produtoService.updateComId(produto, id);
		if (null == produtoAtualizado)
			return new ResponseEntity<>(produtoAtualizado, HttpStatus.BAD_REQUEST);
		else
			return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(Produto produto) {
		produtoService.delete(produto);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProdutoId(Integer id) {
		produtoService.deleteProdutoId(id);
		return new ResponseEntity<>("", HttpStatus.OK);
	}

}