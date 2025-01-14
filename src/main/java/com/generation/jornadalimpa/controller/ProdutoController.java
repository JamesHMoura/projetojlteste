package com.generation.jornadalimpa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.jornadalimpa.model.Produto;
import com.generation.jornadalimpa.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/produtos")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getall() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (produto.getId() != null) {
			Optional<Produto> targetProduct = produtoRepository.findById(produto.getId());
			if (targetProduct.isPresent())
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto com o mesmo Id já existente!");
		}
		return (ResponseEntity.ok(produtoRepository.save(produto)));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		produtoRepository.deleteById(id);
	}
	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		return produtoRepository.findById(produto.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
						.body(produtoRepository.save(produto)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());	
	}
	
//	@GetMapping("/produto/{produto}")
//	public ResponseEntity<List<Produto>> getByProduto(@PathVariable String produto){
//		return ResponseEntity.ok(produtoRepository.findAllByProdutoContainingIgnoreCase(produto));
//	}
//	
//	@GetMapping("/menor-preco")
//	public List<Produto> listarProdutosOrdenadosPorPrecomenor() {
//        return produtoRepository.findAllByOrderByPrecoAsc();
//    }
//	
//	@GetMapping("/maior-preco")
//	public List<Produto> listarProdutosOrdenadosPorPrecomaior() {
//        return produtoRepository.findAllByOrderByPrecoDesc();
//    }
}
