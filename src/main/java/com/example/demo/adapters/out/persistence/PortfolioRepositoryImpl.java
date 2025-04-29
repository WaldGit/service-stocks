package com.example.demo.adapters.out.persistence;

import com.example.demo.application.ports.out.PortfolioRepository;
import com.example.demo.domain.Portfolio;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public class PortfolioRepositoryImpl implements PortfolioRepository {

    private final SpringDataPortfolioRepository springDataPortfolioRepository;

    public PortfolioRepositoryImpl(SpringDataPortfolioRepository springDataPortfolioRepository) {
        this.springDataPortfolioRepository = springDataPortfolioRepository;
    }

    @Override
    public Portfolio findPortfolioWithInvestments(UUID portfolioId) {
        return springDataPortfolioRepository.findPortfolioWithInvestments(portfolioId);
    }

    @Override
    public Optional<Portfolio> findByName(String name) {
        return springDataPortfolioRepository.findByName(name);
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        return springDataPortfolioRepository.save(portfolio);
    }

    @Override
    public List<Portfolio> findAll() {
        return springDataPortfolioRepository.findAll();
    }

    public Optional<Portfolio> findById(UUID id) {
        return springDataPortfolioRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataPortfolioRepository.deleteById(id);
    }
}
