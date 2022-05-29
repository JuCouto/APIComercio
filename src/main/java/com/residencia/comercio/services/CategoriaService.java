package com.residencia.comercio.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.comercio.dtos.CategoriaDTO;
import com.residencia.comercio.entities.Categoria;
import com.residencia.comercio.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Value("${pasta.arquivos.imagem}")
	private String diretorio;
	
	public List<Categoria> findAllCategoria(){
		return categoriaRepository.findAll();
	}
	
	public Categoria findCategoriaById(Integer id) {
		return categoriaRepository.findById(id).isPresent() ?
				categoriaRepository.findById(id).get() : null;
	}

	public CategoriaDTO findCategoriaDTOById(Integer id) {
		Categoria categoria = categoriaRepository.findById(id).isPresent() ?
				categoriaRepository.findById(id).get() : null;
		
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		if(null != categoria) {
			categoriaDTO = converterEntidadeParaDto(categoria);
		}
		return categoriaDTO;
	}
	
	public Categoria saveCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public CategoriaDTO saveCategoriaDTO(CategoriaDTO categoriaDTO) {
			
		Categoria categoria = new Categoria();
		
		categoria.setIdCategoria(categoriaDTO.getIdCategoria());
		Categoria novoCategoria = categoriaRepository.save(categoria);
		
		return converterEntidadeParaDto(novoCategoria);
	}
	
	public Categoria updateCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public void deleteCategoria(Integer id) {
		Categoria inst = categoriaRepository.findById(id).get();
		categoriaRepository.delete(inst);
	}
	
	public void deleteCategoria(Categoria categoria) {
		categoriaRepository.delete(categoria);
	}
	
	private Categoria convertDTOToEntidade(CategoriaDTO categoriaDTO){
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
		categoria.setIdCategoria(categoriaDTO.getIdCategoria());
		return categoria;
	}
		
	private CategoriaDTO converterEntidadeParaDto(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setIdCategoria(categoria.getIdCategoria());
		categoriaDTO.setNomeCategoria(categoria.getNomeCategoria());
		return categoriaDTO;
	}
	
	public Categoria saveCategoriaComFoto(String diretorio, MultipartFile file) {
		Path diretorioPath = Paths.get(this.diretorio, diretorio);
		Path arquivoPath = diretorioPath.resolve(file.getOriginalFilename());
		
		//categoriaRepository.findByNomeImagem(String nome);
		
		Categoria categoriaConvertida = new Categoria();
		try {
			Files.createDirectories(diretorioPath);
			file.transferTo(arquivoPath.toFile());
			//ObjectMapper objMapper = new ObjectMapper();
			//categoriaConvertida = objMapper.readValue(categoriaString, Categoria.class);
		} catch (IOException e) {
			System.out.println("Ocorreu um erro na conversão");
		}
		categoriaConvertida.setNomeImagem(arquivoPath.toString());
		categoriaConvertida.setNomeCategoria(diretorio);
		
		return categoriaRepository.save(categoriaConvertida);
	}
}
