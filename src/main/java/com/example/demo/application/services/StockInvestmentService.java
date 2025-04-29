import com.example.demo.adapters.out.persistence.StockInvestmentRepository;
import com.example.demo.adapters.out.persistence.StockRepository;
import com.example.demo.domain.StockInvestment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockInvestmentService {
    private final StockInvestmentRepository stockInvestmentRepository;
    private final StockRepository stockRepository;

    public List<StockInvestment> getInvestmentsByPortfolioId(UUID portfolioId) {
        return stockInvestmentRepository.findByPortfolio_Id(portfolioId);
    }

    @jakarta.transaction.Transactional
    public StockInvestment addInvestment(StockInvestment investment) {
        return stockInvestmentRepository.save(investment);
    }


}
