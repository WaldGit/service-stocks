import com.example.demo.adapters.out.StockInvestmentRepository;
import com.example.demo.domain.StockInvestment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockInvestmentService {
    private final StockInvestmentRepository stockInvestmentRepository;

    public List<StockInvestment> getInvestmentsByPortfolioId(UUID portfolioId) {
        return stockInvestmentRepository.findByPortfolio_Id(portfolioId);
    }
}
