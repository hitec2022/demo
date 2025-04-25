package com.sparta.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.sparta.demo.model.Board;
import com.sparta.demo.service.BoardService;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/svc")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {
    
    BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("/board/{boardId}")
    public Board getBoard(@PathVariable @NotNull Long boardId) throws Exception{
        log.info("get board boardId : " + boardId);
        Board board = boardService.getBoard(boardId);

        return board;
    }

    @GetMapping("/boards")
    public List<Board> getBoardList() throws Exception{
        List<Board> boards = boardService.getBoardList();
        return boards;
    }

    @PostMapping("/board")
    @ResponseStatus(HttpStatus.CREATED)
    public Board saveBoard(@RequestParam String title, @RequestParam String content) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return boardService.saveBoard(title, content, email);
    }
    
    @DeleteMapping("/board/{boardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable @NotNull Long boardId) throws Exception{
        Board board = boardService.getBoard(boardId);
        boardService.deleteBoard(board);
    }
}
