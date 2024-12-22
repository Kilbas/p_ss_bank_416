package com.bank.publicinfo.util;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import org.apache.commons.lang.RandomStringUtils;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;

public class CreateAtmAndBranch {

    public static AtmDTO createAtmDTO() {
        AtmDTO atmDTO = new AtmDTO();
        atmDTO.setId(createRandomLong());
        atmDTO.setAddress(createRandomString());
        atmDTO.setStartOfWork(LocalTime.now());
        atmDTO.setEndOfWork(LocalTime.now());
        atmDTO.setAllHours(false);
        atmDTO.setBranch(createBranch());
        atmDTO.setIsDeleteBranch(false);
        return atmDTO;
    }

    public static Atm createAtm() {
        Atm atm = new Atm();
        atm.setId(createRandomLong());
        atm.setAddress(createRandomString());
        atm.setStartOfWork(LocalTime.now());
        atm.setEndOfWork(LocalTime.now());
        atm.setAllHours(false);
        atm.setBranch(createBranch());
        return atm;
    }

    public static Branch createBranch() {
        Branch branch = new Branch();
        branch.setId(createRandomLong());
        branch.setAddress(createRandomString());
        branch.setPhoneNumber(createRandomLong());
        branch.setCity(createRandomString());
        branch.setStartOfWork(LocalTime.now());
        branch.setEndOfWork(LocalTime.now());
        branch.setAtms(new HashSet<>());
        return branch;
    }

    public static BranchDTO createBranchDTO() {
        BranchDTO branch = new BranchDTO();
        branch.setId(createRandomLong());
        branch.setAddress(createRandomString());
        branch.setPhoneNumber(createRandomLong());
        branch.setCity(createRandomString());
        branch.setStartOfWork(LocalTime.now());
        branch.setEndOfWork(LocalTime.now());
        branch.setAtms(new HashSet<>());
        return branch;
    }

    private static Long createRandomLong() {
        Random random = new Random();
        return random.nextLong(1000L);
    }

    private static String createRandomString() {
        return RandomStringUtils.random(50, true, true);
    }
}