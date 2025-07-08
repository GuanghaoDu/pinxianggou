package cn.adu.api;


import cn.adu.api.dto.LockMarketPayOrderRequestDTO;
import cn.adu.api.dto.LockMarketPayOrderResponseDTO;
import cn.adu.api.response.Response;

public interface IMarketTradeService {

Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequsetDTO);

}
