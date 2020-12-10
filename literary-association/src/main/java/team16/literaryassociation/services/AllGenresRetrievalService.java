package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.repository.GenreRepository;

@Service
public class AllGenresRetrievalService implements JavaDelegate {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

    }
}
