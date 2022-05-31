package com.residencia.comercio.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.comercio.dtos.ProdutoDTO;
import com.residencia.comercio.entities.Produto;
import com.residencia.comercio.repositories.ProdutoRepository;

@Service
public class ProdutoService {
	@Autowired
	ProdutoRepository produtoRepository;
	
	public List<Produto> findAllProduto(){
		return produtoRepository.findAll();
	}
	
	public Produto findById(Integer id){
		return produtoRepository.findById(id).isPresent() ? produtoRepository.findById(id).get(): null;
	}
	
	public Produto saveProduto(Produto produto) {
		return produtoRepository.save(produto);
	}
	public ProdutoDTO saveProdutoDTO(ProdutoDTO produtoDTO) {
		Produto produto = produtoRepository.save(produtoDTO.converteProdutoParaEntidade());
		return produto.converteParaDTO();
	}
	
	public Produto updateProduto(Produto produto) {
		return produtoRepository.save(produto);
	}
	
	public Produto updateComId(Produto produto, Integer id) {
		Produto produtoBD = produtoRepository.findById(id).isPresent() ? produtoRepository.findById(id).get(): null;
		
		Produto produtoAtualizado = null;
		if(null !=produtoBD) {
			produtoBD.setSku(produto.getSku());
			produtoBD.setFornecedor(produto.getFornecedor());
			produtoBD.setCategoria(produto.getCategoria());
			produtoBD.setNomeProduto(produto.getNomeProduto());
			produtoAtualizado = produtoRepository.save(produtoBD);
		}
		
		return produtoAtualizado;
	}
	
	/*
	 * public void delete(Produto produto) { Produto inst =
	 * produtoRepository.findById(produto.getIdProduto()).get();
	 * produtoRepository.delete(inst); }
	 */
	
	public void deleteProdutoId(Integer id) {
		Produto inst = produtoRepository.findById(id).get();
		produtoRepository.delete(inst);
	}
}
