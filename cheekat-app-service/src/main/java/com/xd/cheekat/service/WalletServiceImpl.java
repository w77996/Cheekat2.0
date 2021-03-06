package com.xd.cheekat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xd.cheekat.common.Constant;
import com.xd.cheekat.dao.WalletDao;
import com.xd.cheekat.dao.WalletLogDao;
import com.xd.cheekat.dao.WalletRecordDao;
import com.xd.cheekat.pojo.Wallet;
import com.xd.cheekat.pojo.WalletLog;
import com.xd.cheekat.pojo.WalletRecord;
import com.xd.cheekat.util.DateUtil;
import com.xd.cheekat.util.PayCommonUtil;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletDao walletDao;
	@Autowired
	private WalletRecordDao walletRecordDao;
	@Autowired
	private WalletLogDao walletLogDao;

	@Override
	public void addNewUser(Long userId) {
		// TODO Auto-generated method stub
		Wallet wallet = new Wallet();
		wallet.setUserId(userId);
		walletDao.insertSelective(wallet);
	}

	@Override
	public Wallet findWalletByUserId(long userId) {
		// TODO Auto-generated method stub
		return walletDao.findWalletByUserId(userId);
	}

	@Transactional
	@Override
	public boolean editUserWalletPayBalance(String out_trade_no, long from_uid,
			int type, Double changemoney, Double money) {
		// TODO Auto-generated method stub

		String date = DateUtil.getNowTime();
		// 更新支付的状态
		WalletRecord walletRecord = new WalletRecord();
		walletRecord.setRecordSn(out_trade_no);
		walletRecord.setPayStatus(Constant.PAY_STATUS_SUCCESS);
		walletRecord.setPayTime(date);
		walletRecordDao.updateWalletRecordByRecordSn(walletRecord);
		// 更新日志
		WalletLog walletLog = new WalletLog();
		walletLog.setRecordSn(out_trade_no);
		walletLog.setUserId(from_uid);
		walletLog.setType(type);
		walletLog.setChangeMoney(changemoney);
		walletLog.setMoney(money);
		walletLog.setCreateTime(date);
		walletLogDao.insertSelective(walletLog);

		// 更新金额
		// int i = walletDao.excuse("update tb_wallet set money =" + money +
		// " where user_id = " + from_uid);
		Wallet wallet = new Wallet();
		wallet.setMoney(money);
		wallet.setUserId(from_uid);

		walletDao.updateMoneyByUserId(wallet);
		return true;

	}

	@Override
	public boolean editUserWalletFetchBalance(String recordSn, Long userId,
			int logType, Double changemoney, Double money) {
		String date = DateUtil.getNowTime();

		// 更新收款的状态
		WalletRecord walletRecord = new WalletRecord();
		walletRecord.setRecordSn(recordSn);
		walletRecord.setToUid(userId);
		walletRecord.setFetchStatus(Constant.PAY_STATUS_SUCCESS);
		walletRecord.setFetchTime(date);
		walletRecordDao.updateWalletRecordByRecordSn(walletRecord);
		// 更新日志
		WalletLog walletLog = new WalletLog();
		walletLog.setRecordSn(recordSn);
		walletLog.setUserId(userId);
		walletLog.setType(logType);
		walletLog.setChangeMoney(changemoney);
		walletLog.setMoney(money);
		walletLog.setCreateTime(date);
		int j = walletLogDao.insertSelective(walletLog);

		Wallet wallet = new Wallet();
		wallet.setMoney(money);
		wallet.setUserId(userId);
		walletDao.updateMoneyByUserId(wallet);
		return true;
	}

	@Transactional
	@Override
	public boolean refund(String recordSn, long userId, int logType,
			Double changeMoney, Double money) {
		String date = DateUtil.getNowTime();
        //更新支付的状态
       // where.and("record_sn", C.EQ, record_sn);
		String newRecordSn = PayCommonUtil.createOutTradeNo();
        WalletRecord walletRecord = new WalletRecord();
        //walletRecord.setType(Constant.ORDER_TYPE_BACK);
        walletRecord.setPayStatus(Constant.PAY_STATUS_BACK);
        walletRecord.setRecordSn(newRecordSn);
        walletRecord.setFromUid(0L);
        walletRecord.setToUid(userId);
        walletRecord.setFetchTime(date);
        walletRecord.setFetchStatus(Constant.FETCH_SUCCESS);
        walletRecordDao.insertSelective(walletRecord);
        //更新订单支付状态
        WalletRecord userWalletRecord = new WalletRecord();
        walletRecord.setPayStatus(Constant.PAY_STATUS_BACK);
        walletRecord.setFetchTime(date);
        walletRecord.setFetchStatus(Constant.FETCH_SUCCESS);
        walletRecord.setRecordSn(recordSn);
        walletRecordDao.updateWalletRecordByRecordSn(userWalletRecord);
        //更新日志
        WalletLog walletLog = new WalletLog();
        walletLog.setRecordSn(recordSn);
        walletLog.setUserId(userId);
        walletLog.setType(logType);
        walletLog.setChangeMoney(changeMoney);
        walletLog.setMoney(money);
        walletLog.setCreateTime(date);
        walletLogDao.insertSelective(walletLog);


        Wallet wallet = new Wallet();
		wallet.setMoney(money);
		wallet.setUserId(userId);
		walletDao.updateMoneyByUserId(wallet);
		return true;
	}

}
