package pl.xsolve.mvp.test;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class BundleMock {
    private Bundle bundle;
    private Map<String, Object> data = new HashMap<>();
    private Answer putAllAnswer;
    private Answer putAnswer;
    private Answer getAnswer;
    private Answer<Boolean> containsKeyAnswer;

    public static Bundle mockBundle() {
        BundleMock bundleMock = new BundleMock();
        return bundleMock.bundle;
    }

    private BundleMock() {
        bundle = mock(Bundle.class);
        createAnswers();
        mockMethods();
    }

    private void createAnswers() {
        putAllAnswer = invocationOnMock -> {
            Bundle sourceBundle = (Bundle) invocationOnMock.getArguments()[0];
            Set<String> keySet = sourceBundle.keySet();
            for (String key : keySet) {
                data.put(key, sourceBundle.get(key));
            }
            return null;
        };

        putAnswer = invocationOnMock -> {
            String key = (String) invocationOnMock.getArguments()[0];
            Object val = invocationOnMock.getArguments()[1];
            data.put(key, val);
            return null;
        };

        getAnswer = invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            String key = (String) args[0];
            if (data.containsKey(key)) {
                return data.get(key);
            } else if (args.length > 1) {
                return args[1];
            } else {
                return null;
            }

        };

        containsKeyAnswer = invocationOnMock -> {
            String key = (String) invocationOnMock.getArguments()[0];
            return data.containsKey(key);
        };
    }

    private void mockMethods() {

        when(bundle.containsKey(anyString())).thenAnswer(containsKeyAnswer);

        when(bundle.keySet()).thenAnswer(invocationOnMock -> data.keySet());

        //all
        doAnswer(putAllAnswer).when(bundle).putAll(any(Bundle.class));

        //boolean
        doAnswer(putAnswer).when(bundle).putBoolean(anyString(), anyBoolean());
        when(bundle.getBoolean(anyString())).thenAnswer(getAnswer);
        when(bundle.getBoolean(anyString(), anyBoolean())).thenAnswer(getAnswer);
        //boolean[]
        doAnswer(putAnswer).when(bundle).putBooleanArray(anyString(), any(boolean[].class));
        when(bundle.getBooleanArray(anyString())).thenAnswer(getAnswer);

        //bundle
        doAnswer(putAnswer).when(bundle).putBundle(anyString(), any(Bundle.class));
        when(bundle.getBundle(anyString())).thenAnswer(getAnswer);

        //byte
        doAnswer(putAnswer).when(bundle).putByte(anyString(), anyByte());
        when(bundle.getByte(anyString())).thenAnswer(getAnswer);
        when(bundle.getByte(anyString(), anyByte())).thenAnswer(getAnswer);
        //byte[]
        doAnswer(putAnswer).when(bundle).putByteArray(anyString(), any(byte[].class));
        when(bundle.getByteArray(anyString())).thenAnswer(getAnswer);

        //Char
        doAnswer(putAnswer).when(bundle).putChar(anyString(), anyChar());
        when(bundle.getChar(anyString())).thenAnswer(getAnswer);
        when(bundle.getChar(anyString(), anyChar())).thenAnswer(getAnswer);
        //Char[]
        doAnswer(putAnswer).when(bundle).putCharArray(anyString(), any(char[].class));
        when(bundle.getCharArray(anyString())).thenAnswer(getAnswer);

        //CharSequence
        doAnswer(putAnswer).when(bundle).putCharSequence(anyString(), any(CharSequence.class));
        when(bundle.getCharSequence(anyString())).thenAnswer(getAnswer);
        when(bundle.getCharSequence(anyString(), any(CharSequence.class))).thenAnswer(getAnswer);
        //CharSequence[]
        doAnswer(putAnswer).when(bundle).putCharSequenceArray(anyString(), any(CharSequence[].class));
        when(bundle.getCharSequenceArray(anyString())).thenAnswer(getAnswer);
        //ArrayList<CharSequence>
        doAnswer(putAnswer).when(bundle).putCharSequenceArrayList(anyString(), any(ArrayList.class));
        when(bundle.getCharSequenceArrayList(anyString())).thenAnswer(getAnswer);

        //double
        doAnswer(putAnswer).when(bundle).putDouble(anyString(), anyDouble());
        when(bundle.getDouble(anyString())).thenAnswer(getAnswer);
        when(bundle.getDouble(anyString(), anyDouble())).thenAnswer(getAnswer);
        //double[]
        doAnswer(putAnswer).when(bundle).putDoubleArray(anyString(), any(double[].class));
        when(bundle.getDoubleArray(anyString())).thenAnswer(getAnswer);

        //Float
        doAnswer(putAnswer).when(bundle).putFloat(anyString(), anyFloat());
        when(bundle.getFloat(anyString())).thenAnswer(getAnswer);
        when(bundle.getFloat(anyString(), anyFloat())).thenAnswer(getAnswer);
        //Float[]
        doAnswer(putAnswer).when(bundle).putFloatArray(anyString(), any(float[].class));
        when(bundle.getFloatArray(anyString())).thenAnswer(getAnswer);

        //int
        doAnswer(putAnswer).when(bundle).putInt(anyString(), anyInt());
        when(bundle.getInt(anyString())).thenAnswer(getAnswer);
        when(bundle.getInt(anyString(), anyInt())).thenAnswer(getAnswer);
        //int[]
        doAnswer(putAnswer).when(bundle).putIntArray(anyString(), any(int[].class));
        when(bundle.getIntArray(anyString())).thenAnswer(getAnswer);
        //ArrayList<Integer>
        doAnswer(putAnswer).when(bundle).putIntegerArrayList(anyString(), any(ArrayList.class));
        when(bundle.getIntegerArrayList(anyString())).thenAnswer(getAnswer);

        //long
        doAnswer(putAnswer).when(bundle).putLong(anyString(), anyLong());
        when(bundle.getLong(anyString())).thenAnswer(getAnswer);
        when(bundle.getLong(anyString(), anyLong())).thenAnswer(getAnswer);
        //long[]
        doAnswer(putAnswer).when(bundle).putLongArray(anyString(), any(long[].class));
        when(bundle.getLongArray(anyString())).thenAnswer(getAnswer);

        //Parcelable
        doAnswer(putAnswer).when(bundle).putParcelable(anyString(), any(Parcelable.class));
        when(bundle.getParcelable(anyString())).thenAnswer(getAnswer);
        //Parcelable[]
        doAnswer(putAnswer).when(bundle).putParcelableArray(anyString(), any(Parcelable[].class));
        when(bundle.getParcelableArray(anyString())).thenAnswer(getAnswer);
        //ArrayList<Parcelable>
        doAnswer(putAnswer).when(bundle).putParcelableArrayList(anyString(), any(ArrayList.class));
        when(bundle.getParcelableArrayList(anyString())).thenAnswer(getAnswer);
        //SparseArray<Parcelable>
        doAnswer(putAnswer).when(bundle).putSparseParcelableArray(anyString(), any(SparseArray.class));
        when(bundle.getSparseParcelableArray(anyString())).thenAnswer(getAnswer);

        //Serializable
        doAnswer(putAnswer).when(bundle).putSerializable(anyString(), any(Serializable.class));
        when(bundle.getSerializable(anyString())).thenAnswer(getAnswer);

        //short
        doAnswer(putAnswer).when(bundle).putShort(anyString(), anyShort());
        when(bundle.getShort(anyString())).thenAnswer(getAnswer);
        when(bundle.getShort(anyString(), anyShort())).thenAnswer(getAnswer);
        //short[]
        doAnswer(putAnswer).when(bundle).putShortArray(anyString(), any(short[].class));
        when(bundle.getShortArray(anyString())).thenAnswer(getAnswer);

        //String
        doAnswer(putAnswer).when(bundle).putString(anyString(), anyString());
        when(bundle.getString(anyString())).thenAnswer(getAnswer);
        when(bundle.getString(anyString(), anyString())).thenAnswer(getAnswer);
        //String[]
        doAnswer(putAnswer).when(bundle).putStringArray(anyString(), any(String[].class));
        when(bundle.getStringArray(anyString())).thenAnswer(getAnswer);
        //ArrayList<String>
        doAnswer(putAnswer).when(bundle).putStringArrayList(anyString(), any(ArrayList.class));
        when(bundle.getStringArrayList(anyString())).thenAnswer(getAnswer);

        when(bundle.get(anyString())).thenAnswer(getAnswer);
    }

}
