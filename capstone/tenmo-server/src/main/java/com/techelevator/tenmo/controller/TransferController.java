package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
    private final static int PENDING_STATUS = 1;
    private final static int APPROVED_STATUS = 2;
    private final static int REJECTED_STATUS = 3;
    private final static int TYPE_SEND = 2;
    private final static int TYPE_REQUEST = 1;

    public TransferController(UserDao userDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/send/", method = RequestMethod.POST)
    public void sendBucks(@RequestBody TransferDTO transfer) {
        int transferId = transferDao.sendRequestBucks(transfer);
        transferDao.updateSenderAccountBalance(transferId, transfer);
        transferDao.updateReceiverAccountBalance(transferId, transfer);
    }

    @RequestMapping(path = "/transfer/history/", method = RequestMethod.GET)
    public List<TransferDTO> userTransfers(Principal principal) {
        String username = principal.getName();
        UserDTO user = userDao.findAccountByUsername(username);
        return transferDao.userTransfers(user);
    }

    @RequestMapping(path = "/request/", method = RequestMethod.POST)
    public void requestBucks(@RequestBody TransferDTO transfer) {
        transferDao.sendRequestBucks(transfer);
    }

    @RequestMapping(path = "/transfer/", method = RequestMethod.PUT)
    public void updateTransfer(@RequestBody TransferDTO transfer) {
        transferDao.updateTransfer(transfer);
        UserDTO payingUser = userDao.findAccountByUsername(transfer.getAccountFromUsername());
        if (transfer.getStatusId() == APPROVED_STATUS && transfer.getAmount().compareTo(payingUser.getBalance()) <= 0) {
            transferDao.updateSenderAccountBalance(transfer.getTransferId(), transfer);
            transferDao.updateReceiverAccountBalance(transfer.getTransferId(), transfer);
        } else {
            transfer.setStatusId(PENDING_STATUS);
        }
    }


}
