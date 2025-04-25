package com.sparta.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.demo.model.Board;
import com.sparta.demo.model.User;
import com.sparta.demo.repository.BoardRepository;
import com.sparta.demo.repository.UserRepository;

@Service
public class BoardService {
    BoardRepository boardRepository;
    UserRepository userRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository){
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Board getBoard(Long id) throws Exception{
        Optional<Board> boardOpt = boardRepository.findById(id);
        return boardOpt.orElseThrow(() -> new Exception("board not found for id " + id));
    }

    @Transactional(readOnly = false)
    public Board saveBoard(String title, String content, String email){
        User user =  userRepository.findByUserEmail(email);

        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setUserName(user.getUserName());
        board.setUserId(user.getId());

        return boardRepository.save(board);
    }

    @Transactional(readOnly = false)
    public void deleteBoard(Board board){
        boardRepository.delete(board);
    }
}
