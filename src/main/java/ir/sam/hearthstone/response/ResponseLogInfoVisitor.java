package ir.sam.hearthstone.response;

public interface ResponseLogInfoVisitor {
    void setPassiveDetailsInfo(PassiveDetails response);

    void setPlayDetailsInfo(PlayDetails playDetails);

    void setGoToInfo(GoTo goTo);

    void setShowMessageInfo(ShowMessage showMessage);

    void setCollectionDetailsInfo(AllCollectionDetails allCollectionDetails);

//    void setFirstCollectionDetailsInfo(FirstCollectionDetails firstCollectionDetails);

    void setStatusDetailsInfo(StatusDetails statusDetails);

    void setShopDetailsInfo(ShopDetails shopDetails);

    void setLoginResponseInfo(LoginResponse loginResponse);
}