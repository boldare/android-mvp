package pl.xsolve.mvp.test;


import android.os.Bundle;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static pl.xsolve.mvp.test.BundleMock.mockBundle;

public class BundleMockTest {
    Bundle systemUnderTest;

    @Before
    public void setUp() throws Exception {
        systemUnderTest = mockBundle();
    }

    @Test
    public void shouldReturnSavedSimpleValues() throws Exception {
        systemUnderTest.putBoolean("boolean-true", true);
        systemUnderTest.putBoolean("boolean-false", false);
        systemUnderTest.putBooleanArray("boolean-arr", new boolean[]{true, false});
        systemUnderTest.putByte("byte", (byte) 100);
        systemUnderTest.putByteArray("byte-arr", new byte[]{1, 2});
        systemUnderTest.putChar("char", 'x');
        systemUnderTest.putCharArray("char-arr", new char[]{'x', 'y'});
        systemUnderTest.putCharSequence("char-seq", "charSeq");
        systemUnderTest.putCharSequenceArray("char-seq-arr", new CharSequence[]{"charSeqA", "charSeqB"});
        systemUnderTest.putCharSequenceArrayList("char-seq-arr-list", new ArrayList<>(Arrays.asList(new CharSequence[]{"charSeqA", "charSeqB"})));
        systemUnderTest.putDouble("double", 100);
        systemUnderTest.putDoubleArray("double-arr", new double[]{100, 101});
        systemUnderTest.putFloat("float", 10);
        systemUnderTest.putFloatArray("float-arr", new float[]{10, 11});
        systemUnderTest.putInt("int", 20);
        systemUnderTest.putIntArray("int-arr", new int[]{20, 21});
        systemUnderTest.putIntegerArrayList("int-arr-list", new ArrayList<>(Arrays.asList(new Integer[]{20, 21})));
        systemUnderTest.putLong("long", 30);
        systemUnderTest.putLongArray("long-arr", new long[]{30, 31});
        systemUnderTest.putShort("short", (short) 40);
        systemUnderTest.putShortArray("short-arr", new short[]{40, 41});
        systemUnderTest.putString("string", "someString");
        systemUnderTest.putStringArray("string-arr", new String[]{"someString", "otherString"});
        systemUnderTest.putStringArrayList("string-arr-list", new ArrayList<>(Arrays.asList(new String[]{"someString", "otherString"})));

        assertThat(systemUnderTest.getBoolean("boolean-true")).isEqualTo(true);
        assertThat(systemUnderTest.getBoolean("boolean-true", false)).isEqualTo(true);
        assertThat(systemUnderTest.getBoolean("boolean-true", true)).isEqualTo(true);
        assertThat(systemUnderTest.get("boolean-true")).isEqualTo(true);

        assertThat(systemUnderTest.getBoolean("boolean-false")).isEqualTo(false);
        assertThat(systemUnderTest.getBoolean("boolean-false", false)).isEqualTo(false);
        assertThat(systemUnderTest.getBoolean("boolean-false", true)).isEqualTo(false);
        assertThat(systemUnderTest.get("boolean-false")).isEqualTo(false);

        assertThat(systemUnderTest.getBoolean("boolean-empty", true)).isEqualTo(true);
        assertThat(systemUnderTest.getBoolean("boolean-empty", false)).isEqualTo(false);
        assertThat(systemUnderTest.getBoolean("boolean-empty")).isEqualTo(false);

        assertThat(systemUnderTest.getBooleanArray("boolean-arr")).isEqualTo(new boolean[]{true, false});
        assertThat(systemUnderTest.getBooleanArray("boolean-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("boolean-arr")).isEqualTo(new boolean[]{true, false});

        assertThat(systemUnderTest.getByte("byte")).isEqualTo((byte) 100);
        assertThat(systemUnderTest.getByte("byte", (byte) 101)).isEqualTo((byte) 100);
        assertThat(systemUnderTest.getByte("byte-empty", (byte) 101)).isEqualTo((byte) 101);
        assertThat(systemUnderTest.getByte("byte-empty")).isEqualTo((byte) 0);
        assertThat(systemUnderTest.get("byte")).isEqualTo((byte) 100);

        assertThat(systemUnderTest.getByteArray("byte-arr")).isEqualTo(new byte[]{1, 2});
        assertThat(systemUnderTest.getByteArray("byte-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("byte-arr")).isEqualTo(new byte[]{1, 2});

        assertThat(systemUnderTest.getChar("char")).isEqualTo('x');
        assertThat(systemUnderTest.getChar("char", 'y')).isEqualTo('x');
        assertThat(systemUnderTest.getChar("char-empty", 'y')).isEqualTo('y');
        assertThat(systemUnderTest.getChar("char-empty")).isEqualTo((char) 0);
        assertThat(systemUnderTest.get("char")).isEqualTo('x');

        assertThat(systemUnderTest.getCharArray("char-arr")).isEqualTo(new char[]{'x', 'y'});
        assertThat(systemUnderTest.getCharArray("char-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("char-arr")).isEqualTo(new char[]{'x', 'y'});

        assertThat(systemUnderTest.getCharSequence("char-seq")).isEqualTo("charSeq");
        assertThat(systemUnderTest.getCharSequence("char-seq", "charSeqOther")).isEqualTo("charSeq");
        assertThat(systemUnderTest.getCharSequence("char-seq-empty", "charSeqOther")).isEqualTo("charSeqOther");
        assertThat(systemUnderTest.getCharSequence("char-seq-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("char-seq")).isEqualTo("charSeq");

        assertThat(systemUnderTest.getCharSequenceArray("char-seq-arr")).isEqualTo(new CharSequence[]{"charSeqA", "charSeqB"});
        assertThat(systemUnderTest.getCharSequenceArray("char-seq-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("char-seq-arr")).isEqualTo(new CharSequence[]{"charSeqA", "charSeqB"});

        assertThat(systemUnderTest.getCharSequenceArrayList("char-seq-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new CharSequence[]{"charSeqA", "charSeqB"})));
        assertThat(systemUnderTest.getCharSequenceArrayList("char-seq-arr-list-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("char-seq-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new CharSequence[]{"charSeqA", "charSeqB"})));

        assertThat(systemUnderTest.getDouble("double")).isEqualTo(100);
        assertThat(systemUnderTest.getDouble("double-empty", 101)).isEqualTo(101);
        assertThat(systemUnderTest.getDouble("double-empty")).isEqualTo(0);
        assertThat(systemUnderTest.getDouble("double")).isEqualTo(100);
        assertThat(systemUnderTest.get("double")).isEqualTo(100.0);

        assertThat(systemUnderTest.getDoubleArray("double-arr")).isEqualTo(new double[]{100, 101});
        assertThat(systemUnderTest.getDoubleArray("double-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("double-arr")).isEqualTo(new double[]{100, 101});

        assertThat(systemUnderTest.getFloat("float")).isEqualTo(10);
        assertThat(systemUnderTest.getFloat("float", 11)).isEqualTo(10);
        assertThat(systemUnderTest.getFloat("float-empty", 11)).isEqualTo(11);
        assertThat(systemUnderTest.getFloat("float-empty")).isEqualTo(0);
        assertThat(systemUnderTest.get("float")).isEqualTo(10f);

        assertThat(systemUnderTest.getFloatArray("float-arr")).isEqualTo(new float[]{10, 11});
        assertThat(systemUnderTest.getFloatArray("float-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("float-arr")).isEqualTo(new float[]{10, 11});

        assertThat(systemUnderTest.getInt("int")).isEqualTo(20);
        assertThat(systemUnderTest.getInt("int", 21)).isEqualTo(20);
        assertThat(systemUnderTest.getInt("int-empty", 21)).isEqualTo(21);
        assertThat(systemUnderTest.getInt("int-empty")).isEqualTo(0);
        assertThat(systemUnderTest.get("int")).isEqualTo(20);

        assertThat(systemUnderTest.getIntArray("int-arr")).isEqualTo(new int[]{20, 21});
        assertThat(systemUnderTest.getIntArray("int-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("int-arr")).isEqualTo(new int[]{20, 21});

        assertThat(systemUnderTest.getIntegerArrayList("int-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new Integer[]{20, 21})));
        assertThat(systemUnderTest.getIntegerArrayList("int-arr-list-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("int-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new Integer[]{20, 21})));

        assertThat(systemUnderTest.getLong("long")).isEqualTo(30);
        assertThat(systemUnderTest.getLong("long", 31)).isEqualTo(30);
        assertThat(systemUnderTest.getLong("long-empty", 31)).isEqualTo(31);
        assertThat(systemUnderTest.getLong("long-empty")).isEqualTo(0);
        assertThat(systemUnderTest.get("long")).isEqualTo(30l);

        assertThat(systemUnderTest.getLongArray("long-arr")).isEqualTo(new long[]{30, 31});
        assertThat(systemUnderTest.getLongArray("long-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("long-arr")).isEqualTo(new long[]{30, 31});

        assertThat(systemUnderTest.getShort("short")).isEqualTo((short) 40);
        assertThat(systemUnderTest.getShort("short", (short) 41)).isEqualTo((short) 40);
        assertThat(systemUnderTest.getShort("short-empty", (short) 41)).isEqualTo((short) 41);
        assertThat(systemUnderTest.getShort("short-empty")).isEqualTo((short) 0);
        assertThat(systemUnderTest.get("short")).isEqualTo((short) 40);

        assertThat(systemUnderTest.getShortArray("short-arr")).isEqualTo(new short[]{40, 41});
        assertThat(systemUnderTest.getShortArray("short-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("short-arr")).isEqualTo(new short[]{40, 41});

        assertThat(systemUnderTest.getString("string")).isEqualTo("someString");
        assertThat(systemUnderTest.getString("string", "otherString")).isEqualTo("someString");
        assertThat(systemUnderTest.getString("string-empty", "otherString")).isEqualTo("otherString");
        assertThat(systemUnderTest.getString("string-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("string")).isEqualTo("someString");

        assertThat(systemUnderTest.getStringArray("string-arr")).isEqualTo(new String[]{"someString", "otherString"});
        assertThat(systemUnderTest.getStringArray("string-arr-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("string-arr")).isEqualTo(new String[]{"someString", "otherString"});

        assertThat(systemUnderTest.getStringArrayList("string-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new String[]{"someString", "otherString"})));
        assertThat(systemUnderTest.getStringArrayList("string-arr-list-empty")).isEqualTo(null);
        assertThat(systemUnderTest.get("string-arr-list")).isEqualTo(new ArrayList<>(Arrays.asList(new String[]{"someString", "otherString"})));
    }

    @Test
    public void shouldReturnSavedComplexBundle() throws Exception {
        Bundle bundle = mock(Bundle.class);
        Parcelable parcelableA = mock(Parcelable.class);
        Parcelable parcelableB = mock(Parcelable.class);
        Serializable serializable = mock(Serializable.class);

        systemUnderTest.putBundle("bundle", bundle);
        systemUnderTest.putParcelable("parcelable", parcelableA);
        systemUnderTest.putParcelableArray("parcelable-arr", new Parcelable[]{parcelableA, parcelableB});
        systemUnderTest.putParcelableArrayList("parcelable-arr-list", new ArrayList<>(Arrays.asList(
                new Parcelable[]{parcelableA, parcelableB})));
        systemUnderTest.putSerializable("serializable", serializable);

        assertThat((Object)systemUnderTest.getBundle("bundle")).isEqualTo(bundle);
        assertThat((Object)systemUnderTest.getParcelable("parcelable")).isEqualTo(parcelableA);
        assertThat((Object)systemUnderTest.getParcelableArray("parcelable-arr")).isEqualTo(new Parcelable[]{parcelableA, parcelableB});
        assertThat((Object)systemUnderTest.getParcelableArrayList("parcelable-arr-list")).isEqualTo(
                new ArrayList<>(Arrays.asList(
                        new Parcelable[]{parcelableA, parcelableB})));
        assertThat((Object)systemUnderTest.getSerializable("serializable")).isEqualTo(serializable);
    }

    @Test
    public void shouldReturnSavedAllValues() throws Exception {
        Bundle otherBundle = mockBundle();
        otherBundle.putString("string", "someString");
        otherBundle.putInt("int", 11);

        systemUnderTest.putAll(otherBundle);

        assertThat(systemUnderTest.get("string")).isEqualTo("someString");
        assertThat(systemUnderTest.get("int")).isEqualTo(11);
    }

    @Test
    public void shouldReturnValidKeysInfo() throws Exception {
        systemUnderTest.putString("string", "someString");
        systemUnderTest.putInt("int", 11);
        systemUnderTest.putChar("char", 'x');

        assertThat(systemUnderTest.containsKey("string")).isEqualTo(true);
        assertThat(systemUnderTest.containsKey("int")).isEqualTo(true);
        assertThat(systemUnderTest.containsKey("other-key")).isEqualTo(false);

        assertThat(systemUnderTest.keySet()).containsExactlyInAnyOrder("string", "int", "char");
    }
}
