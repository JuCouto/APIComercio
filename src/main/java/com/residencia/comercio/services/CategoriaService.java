package com.residencia.comercio.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	ArquivoService arquivoService;
	
	@Autowired
	MailService emailService;
	
	/*exemplo de criar pasta de imagens
	 * @Value("${pasta.arquivos.imagem}")
	 *  private String diretorio;
	 */
	
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
		categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
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
	
	public Categoria saveCategoriaComFoto(String categoriaString, MultipartFile file) throws Exception {
        Categoria categoriaConvertida = new Categoria();
        try {
            ObjectMapper objMapper = new ObjectMapper();
            categoriaConvertida = objMapper.readValue(categoriaString, Categoria.class);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro na gravação");
        }
        Categoria categoriaBD = categoriaRepository.save(categoriaConvertida);
        categoriaBD.setNomeImagem(categoriaBD.getIdCategoria() + "" + file.getOriginalFilename());
        Categoria categoriaAtualizada = categoriaRepository.save(categoriaBD);

        try {
            arquivoService.criarArquivo(categoriaBD.getIdCategoria() + "" + file.getOriginalFilename(), file);
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro ao tentar copiar o arquivo. - " + e.getStackTrace());
        }

        //"cuidado para definir um destinatário válido"
		String corpoEmail = "Foi cadastrada uma categoria" + categoriaAtualizada.toString();
		emailService.enviarEmailTexto("juliana.couto@aluno.senai.br", "Cadastro da Categoria", corpoEmail);
        return categoriaAtualizada;
    }
		/* Código antigo
		 * Categoria categoriaConvertida = new Categoria();
		 *  try {
		 * Files.createDirectories(diretorioPath);
		 * file.transferTo(arquivoPath.toFile());
		 * 
		 * } catch (IOException e) { Path diretorioPath = Paths.get(this.diretorio,
		 * diretorio); 
		 * Path arquivoPath =
		 * diretorioPath.resolve(file.getOriginalFilename());
		 * System.out.println("Ocorreu um erro ao tentar criar o arquivo"); }
		 * categoriaConvertida.setNomeImagem(arquivoPath.toString());
		 * categoriaConvertida.setNomeCategoria(diretorio);
		 * 
		 * return categoriaRepository.save(categoriaConvertida);
		 */
	
}
