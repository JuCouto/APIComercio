package com.residencia.comercio.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.residencia.comercio.dtos.CadastroCepDTO;
import com.residencia.comercio.dtos.CadastroEmpresaReceitaDTO;
import com.residencia.comercio.dtos.FornecedorDTO;
import com.residencia.comercio.entities.Fornecedor;
import com.residencia.comercio.repositories.FornecedorRepository;

@Service
public class FornecedorService {
	@Autowired
	FornecedorRepository fornecedorRepository;
	
	public List<Fornecedor> findAllFornecedor(){
		return fornecedorRepository.findAll();
	}
	
	public Fornecedor findFornecedorById(Integer id) {
		return fornecedorRepository.findById(id).isPresent() ?
				fornecedorRepository.findById(id).get() : null;
	}

	public FornecedorDTO findFornecedorDTOById(Integer id) {
		Fornecedor fornecedor = fornecedorRepository.findById(id).isPresent() ?
				fornecedorRepository.findById(id).get() : null;
		
		FornecedorDTO fornecedorDTO = new FornecedorDTO();
		if(null != fornecedor) {
			fornecedorDTO = fornecedor.converterEntidadeParaDto();
		}
		return fornecedorDTO;
	}
	
	public Fornecedor saveFornecedor(Fornecedor fornecedor) {
		return fornecedorRepository.save(fornecedor);
	}
	
	public FornecedorDTO saveFornecedorDTO(FornecedorDTO fornecedorDTO) {
			
		Fornecedor fornecedor = convertDTOToEntidade(fornecedorDTO);
		Fornecedor novoFornecedor = fornecedorRepository.save(fornecedor);
		
		return novoFornecedor.converterEntidadeParaDto();
	}
	
	public Fornecedor saveFornecedorCnpj(String cnpj) throws ParseException {
		Fornecedor novoFornecedor = fornecedorCnpj(cnpj);
		return fornecedorRepository.save(novoFornecedor);
	}
	
	/*public Fornecedor updateFornecedorCep(String cep) {
	Fornecedor novoFornecedor = fornecedorCep(cep);
	return fornecedorRepository.save(novoFornecedor);	
}*/
	
	public Fornecedor updateFornecedor(Fornecedor fornecedor) {
		return fornecedorRepository.save(fornecedor);
	}
	
	public void deleteFornecedor(Integer id) {
		Fornecedor inst = fornecedorRepository.findById(id).get();
		fornecedorRepository.delete(inst);
	}
	
	public void deleteFornecedor(Fornecedor fornecedor) {
		fornecedorRepository.delete(fornecedor);
	}
	
	private Fornecedor convertDTOToEntidade(FornecedorDTO fornecedorDTO){
		Fornecedor fornecedor = new Fornecedor();
		
		fornecedor.setBairro(fornecedorDTO.getBairro());
		fornecedor.setCep(fornecedorDTO.getCep());
		fornecedor.setCnpj(fornecedorDTO.getCnpj());
		fornecedor.setComplemento(fornecedorDTO.getComplemento());
		fornecedor.setDataAbertura(fornecedorDTO.getDataAbertura());
		fornecedor.setEmail(fornecedorDTO.getEmail());
		fornecedor.setIdFornecedor(fornecedorDTO.getIdFornecedor());
		fornecedor.setLogradouro(fornecedorDTO.getLogradouro());
		fornecedor.setMunicipio(fornecedorDTO.getMunicipio());
		fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
		fornecedor.setNumero(fornecedorDTO.getNumero());
		fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
		fornecedor.setStatusSituacao(fornecedorDTO.getStatusSituacao());
		fornecedor.setTelefone(fornecedorDTO.getTelefone());
		fornecedor.setTipo(fornecedorDTO.getTipo());
		fornecedor.setUf(fornecedorDTO.getUf());
		
		return fornecedor;
	}
	
	public CadastroEmpresaReceitaDTO consultarDadosPorCnpj(String cnpj) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://www.receitaws.com.br/v1/cnpj/{cnpj}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("cnpj", cnpj);

		CadastroEmpresaReceitaDTO receitaWsDadosCnpjVO = 
				restTemplate.getForObject(uri,CadastroEmpresaReceitaDTO.class, params);

		return receitaWsDadosCnpjVO;
	}
	
	public CadastroCepDTO consultarCep(String cep) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://viacep.com.br/ws/{cep}/json/";
		Map<String, String> params = new HashMap<String, String>();
		params.put("cep", cep);

		CadastroCepDTO receitaWsDadosCepVO = 
				restTemplate.getForObject(uri,CadastroCepDTO.class, params);

		return receitaWsDadosCepVO;
	}
	
	public Fornecedor fornecedorCnpj(String cer) throws ParseException {
        CadastroEmpresaReceitaDTO cert = consultarDadosPorCnpj(cer);
        Fornecedor fornecedorCnpj = new Fornecedor();

        fornecedorCnpj.setBairro(cert.getBairro());
        fornecedorCnpj.setCep(cert.getCep());
        fornecedorCnpj.setComplemento(cert.getComplemento());
        fornecedorCnpj.setCnpj(cert.getCnpj());
        fornecedorCnpj.setEmail(cert.getEmail());
        fornecedorCnpj.setLogradouro(cert.getLogradouro());
        fornecedorCnpj.setMunicipio(cert.getMunicipio());
        fornecedorCnpj.setNomeFantasia(cert.getFantasia());
        fornecedorCnpj.setStatusSituacao(cert.getSituacao());
        fornecedorCnpj.setTipo(cert.getTipo());
        fornecedorCnpj.setTelefone(cert.getTelefone());
        fornecedorCnpj.setRazaoSocial(cert.getNome());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
        Date dataFormatada = formato.parse(cert.getAbertura()); 
        fornecedorCnpj.setDataAbertura(dataFormatada);
        fornecedorCnpj.setUf(cert.getUf());
        fornecedorCnpj.setNumero(cert.getNumero());

        return fornecedorCnpj;
    }
	
	public Fornecedor fornecedorCep(String cep) {
		CadastroCepDTO cert = consultarCep(cep);
		Fornecedor fornecedor = new Fornecedor();

		fornecedor.setCep(cert.getCep());
		fornecedor.setBairro(cert.getBairro());
		fornecedor.setComplemento(cert.getComplemento());
		fornecedor.setLogradouro(cert.getLogradouro());
		fornecedor.setUf(cert.getUf());

		return fornecedor;
	}
}
