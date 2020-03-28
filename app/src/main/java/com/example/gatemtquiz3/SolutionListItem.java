package com.example.gatemtquiz3;

import android.os.Parcel;
import android.os.Parcelable;

public class SolutionListItem implements Parcelable {
    private Integer idNo;
    private String solutionImageName;

    public SolutionListItem(Integer idNo, String solutionImageName) {
        this.idNo = idNo;
        this.solutionImageName = solutionImageName;
    }

    protected SolutionListItem(Parcel in) {
        if (in.readByte() == 0) {
            idNo = null;
        } else {
            idNo = in.readInt();
        }
        solutionImageName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idNo);
        }
        dest.writeString(solutionImageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SolutionListItem> CREATOR = new Creator<SolutionListItem>() {
        @Override
        public SolutionListItem createFromParcel(Parcel in) {
            return new SolutionListItem(in);
        }

        @Override
        public SolutionListItem[] newArray(int size) {
            return new SolutionListItem[size];
        }
    };

    public Integer getIdNo() {
        return idNo;
    }

    public void setIdNo(Integer idNo) {
        this.idNo = idNo;
    }

    public String getSolutionImageName() {
        return solutionImageName;
    }

    public void setSolutionImageName(String solutionImageName) {
        this.solutionImageName = solutionImageName;
    }
}
