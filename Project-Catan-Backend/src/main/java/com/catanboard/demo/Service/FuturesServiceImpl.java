package com.catanboard.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catanboard.demo.Exceptions.FutureNotFoundException;
import com.catanboard.demo.POJO.Futures;
import com.catanboard.demo.Repository.FuturesRepository;
import com.catanboard.demo.Repository.PlayerRepository;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FuturesServiceImpl implements FuturesService{

    @Autowired
    FuturesRepository futuresRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Futures getFuture(Long futureid) {
        Optional<Futures> future = futuresRepository.findById(futureid);
        return unwrapFuture(future, futureid);
    }

    @Override
    public Futures saveFuture(Futures future) {
        return futuresRepository.save(future);
    }

    @Override
    public List<Futures> getPlayerFutures(String player_id) {
        List<Futures> initiated = futuresRepository.findByInitiatorId(player_id);
        initiated.addAll(futuresRepository.findByAcceptorId(player_id));
        return initiated;
    }

    @Override
    public HttpStatus deleteFuture(Long id, String code) {
        if(code.equals(playerRepository.findByName("ADMIN").getCode())){
            futuresRepository.deleteById(id);
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Override
    public List<Futures> getAllFutures() {
        return (List<Futures>)futuresRepository.findAll();
    }

    @Override
    public Futures updateFutureAcceptor(Long futureId, String acceptorId) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setAcceptorId(acceptorId);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public Futures updateFutureInitiator(Long futureId, String initiatorId) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setInitiatorId(initiatorId);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public Futures updateFutureAcceptorItems(Long futureId, String acceptorItems) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setAcceptorItems(acceptorItems);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public Futures updateFutureInitiatorItems(Long futureId, String initiatorItems) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setInitiatorItems(initiatorItems);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public Futures updateFutureStatus(Long futureId, int status, String code) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        if((playerRepository.findById(Long.parseLong(unwrappedFuture.getAcceptorId())).get().getCode()).equals(code)){
            unwrappedFuture.setStatus(status);
        }  
        return futuresRepository.save(unwrappedFuture);

    }

    @Override
    public Futures updateFutureAcceptorCollateral(Long futureId, String acceptorCollateral) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setAcceptorCollateral(acceptorCollateral);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public Futures updateFutureInitiatorCollateral(Long futureId, String initiatorCollateral) {
        Optional<Futures> future = futuresRepository.findById(futureId);
        Futures unwrappedFuture =  unwrapFuture(future, futureId);
        unwrappedFuture.setInitiatorCollateral(initiatorCollateral);
        return futuresRepository.save(unwrappedFuture);
    }

    @Override
    public List<Futures> getAcceptorPendingFutures(String player_id) {
        return futuresRepository.findByAcceptorIdAndStatus(player_id, -1);
    }

    static Futures unwrapFuture(Optional<Futures> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new FutureNotFoundException(id);
    }

    @Override
    public List<Futures> getInitiatorProposedFutures(String player_id) {
        return futuresRepository.findByInitiatorIdAndStatus(player_id, -1);
    }

    @Override
    public List<Futures> getInitiatorActiveFutures(String player_id) {
        return futuresRepository.findByInitiatorIdAndStatus(player_id, 1);
    }

    @Override
    public HttpStatus generalUpdateFutures(Long futureId, Futures future, String authorizationCode){
        if(authorizationCode.equals(playerRepository.findByName("ADMIN").getCode())){
            futuresRepository.save(future);
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.UNAUTHORIZED;
        }
    }
}
