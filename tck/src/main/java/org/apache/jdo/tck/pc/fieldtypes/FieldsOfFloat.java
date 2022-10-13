/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;

public class FieldsOfFloat {
  public int identifier;
  private Float Float0;
  private Float Float1;
  private Float Float2;
  private Float Float3;
  private Float Float4;
  private Float Float5;
  private Float Float6;
  private Float Float7;
  private static Float Float8;
  private transient Float Float9;
  private transient Float Float10;
  private transient Float Float11;
  private transient Float Float12;
  private transient Float Float13;
  private transient Float Float14;
  private final Float Float15 = Float.valueOf((float) 5);
  private volatile Float Float16;
  private volatile Float Float17;
  private volatile Float Float18;
  private volatile Float Float19;
  private volatile Float Float20;
  private volatile Float Float21;
  private volatile Float Float22;
  private volatile Float Float23;
  private static transient Float Float24;
  private static final Float Float25 = Float.valueOf((float) 5);
  private static volatile Float Float26;
  private final transient Float Float27 = Float.valueOf((float) 5);
  private transient volatile Float Float28;
  private transient volatile Float Float29;
  private transient volatile Float Float30;
  private transient volatile Float Float31;
  private transient volatile Float Float32;
  private transient volatile Float Float33;
  private static final transient Float Float34 = Float.valueOf((float) 5);
  private static transient volatile Float Float35;
  public Float Float36;
  public Float Float37;
  public Float Float38;
  public Float Float39;
  public Float Float40;
  public Float Float41;
  public Float Float42;
  public Float Float43;
  public static Float Float44;
  public transient Float Float45;
  public transient Float Float46;
  public transient Float Float47;
  public transient Float Float48;
  public transient Float Float49;
  public transient Float Float50;
  public final Float Float51 = Float.valueOf((float) 5);
  public volatile Float Float52;
  public volatile Float Float53;
  public volatile Float Float54;
  public volatile Float Float55;
  public volatile Float Float56;
  public volatile Float Float57;
  public volatile Float Float58;
  public volatile Float Float59;
  public static transient Float Float60;
  public static final Float Float61 = Float.valueOf((float) 5);
  public static volatile Float Float62;
  public final transient Float Float63 = Float.valueOf((float) 5);
  public transient volatile Float Float64;
  public transient volatile Float Float65;
  public transient volatile Float Float66;
  public transient volatile Float Float67;
  public transient volatile Float Float68;
  public transient volatile Float Float69;
  public static final transient Float Float70 = Float.valueOf((float) 5);
  public static transient volatile Float Float71;
  protected Float Float72;
  protected Float Float73;
  protected Float Float74;
  protected Float Float75;
  protected Float Float76;
  protected Float Float77;
  protected Float Float78;
  protected Float Float79;
  protected static Float Float80;
  protected transient Float Float81;
  protected transient Float Float82;
  protected transient Float Float83;
  protected transient Float Float84;
  protected transient Float Float85;
  protected transient Float Float86;
  protected final Float Float87 = Float.valueOf((float) 5);
  protected volatile Float Float88;
  protected volatile Float Float89;
  protected volatile Float Float90;
  protected volatile Float Float91;
  protected volatile Float Float92;
  protected volatile Float Float93;
  protected volatile Float Float94;
  protected volatile Float Float95;
  protected static transient Float Float96;
  protected static final Float Float97 = Float.valueOf((float) 5);
  protected static volatile Float Float98;
  protected final transient Float Float99 = Float.valueOf((float) 5);
  protected transient volatile Float Float100;
  protected transient volatile Float Float101;
  protected transient volatile Float Float102;
  protected transient volatile Float Float103;
  protected transient volatile Float Float104;
  protected transient volatile Float Float105;
  protected static final transient Float Float106 = Float.valueOf((float) 5);
  protected static transient volatile Float Float107;
  Float Float108;
  Float Float109;
  Float Float110;
  Float Float111;
  Float Float112;
  Float Float113;
  Float Float114;
  Float Float115;
  static Float Float116;
  transient Float Float117;
  transient Float Float118;
  transient Float Float119;
  transient Float Float120;
  transient Float Float121;
  transient Float Float122;
  final Float Float123 = Float.valueOf((float) 5);
  volatile Float Float124;
  volatile Float Float125;
  volatile Float Float126;
  volatile Float Float127;
  volatile Float Float128;
  volatile Float Float129;
  volatile Float Float130;
  volatile Float Float131;
  static transient Float Float132;
  static final Float Float133 = Float.valueOf((float) 5);
  static volatile Float Float134;
  final transient Float Float135 = Float.valueOf((float) 5);
  transient volatile Float Float136;
  transient volatile Float Float137;
  transient volatile Float Float138;
  transient volatile Float Float139;
  transient volatile Float Float140;
  transient volatile Float Float141;
  static final transient Float Float142 = Float.valueOf((float) 5);
  static transient volatile Float Float143;

  public static final boolean[] isPersistent = {
    true, true, true, false, true, true, true, false, false, false,
    false, true, true, true, false, false, true, true, true, false,
    true, true, true, false, false, false, false, false, false, false,
    true, true, true, false, false, false, true, true, true, false,
    true, true, true, false, false, false, false, true, true, true,
    false, false, true, true, true, false, true, true, true, false,
    false, false, false, false, false, false, true, true, true, false,
    false, false, true, true, true, false, true, true, true, false,
    false, false, false, true, true, true, false, false, true, true,
    true, false, true, true, true, false, false, false, false, false,
    false, false, true, true, true, false, false, false, true, true,
    true, false, true, true, true, false, false, false, false, true,
    true, true, false, false, true, true, true, false, true, true,
    true, false, false, false, false, false, false, false, true, true,
    true, false, false, false
  };

  public static final boolean[] isStatic = {
    false, false, false, false, false, false, false, false, true, false,
    false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, true, true, true, false, false, false,
    false, false, false, false, true, true, false, false, false, false,
    false, false, false, false, true, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false,
    true, true, true, false, false, false, false, false, false, false,
    true, true, false, false, false, false, false, false, false, false,
    true, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, true, true, true, false,
    false, false, false, false, false, false, true, true, false, false,
    false, false, false, false, false, false, true, false, false, false,
    false, false, false, false, false, false, false, false, false, false,
    false, false, true, true, true, false, false, false, false, false,
    false, false, true, true
  };

  public static final boolean[] isFinal = {
    false, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, true, false, false, false, false,
    false, false, false, false, false, true, false, true, false, false,
    false, false, false, false, true, false, false, false, false, false,
    false, false, false, false, false, false, false, false, false, false,
    false, true, false, false, false, false, false, false, false, false,
    false, true, false, true, false, false, false, false, false, false,
    true, false, false, false, false, false, false, false, false, false,
    false, false, false, false, false, false, false, true, false, false,
    false, false, false, false, false, false, false, true, false, true,
    false, false, false, false, false, false, true, false, false, false,
    false, false, false, false, false, false, false, false, false, false,
    false, false, false, true, false, false, false, false, false, false,
    false, false, false, true, false, true, false, false, false, false,
    false, false, true, false
  };

  public static final String[] fieldSpecs = {
    "private Float Float0",
    "embedded= true   private Float Float1",
    "embedded= false   private Float Float2",
    "persistence-modifier= none    private Float Float3",
    "persistence-modifier= persistent    private Float Float4",
    "persistence-modifier= persistent  embedded= true   private Float Float5",
    "persistence-modifier= persistent  embedded= false   private Float Float6",
    "persistence-modifier= transactional    private Float Float7",
    "private static Float Float8",
    "private transient Float Float9",
    "persistence-modifier= none    private transient Float Float10",
    "persistence-modifier= persistent    private transient Float Float11",
    "persistence-modifier= persistent  embedded= true   private transient Float Float12",
    "persistence-modifier= persistent  embedded= false   private transient Float Float13",
    "persistence-modifier= transactional    private transient Float Float14",
    "private final Float Float15",
    "private volatile Float Float16",
    "embedded= true   private volatile Float Float17",
    "embedded= false   private volatile Float Float18",
    "persistence-modifier= none    private volatile Float Float19",
    "persistence-modifier= persistent    private volatile Float Float20",
    "persistence-modifier= persistent  embedded= true   private volatile Float Float21",
    "persistence-modifier= persistent  embedded= false   private volatile Float Float22",
    "persistence-modifier= transactional    private volatile Float Float23",
    "private static transient Float Float24",
    "private static final Float Float25",
    "private static volatile Float Float26",
    "private transient final Float Float27",
    "private transient volatile Float Float28",
    "persistence-modifier= none    private transient volatile Float Float29",
    "persistence-modifier= persistent    private transient volatile Float Float30",
    "persistence-modifier= persistent  embedded= true   private transient volatile Float Float31",
    "persistence-modifier= persistent  embedded= false   private transient volatile Float Float32",
    "persistence-modifier= transactional    private transient volatile Float Float33",
    "private static transient final Float Float34",
    "private static transient volatile Float Float35",
    "public Float Float36",
    "embedded= true   public Float Float37",
    "embedded= false   public Float Float38",
    "persistence-modifier= none    public Float Float39",
    "persistence-modifier= persistent    public Float Float40",
    "persistence-modifier= persistent  embedded= true   public Float Float41",
    "persistence-modifier= persistent  embedded= false   public Float Float42",
    "persistence-modifier= transactional    public Float Float43",
    "public static Float Float44",
    "public transient Float Float45",
    "persistence-modifier= none    public transient Float Float46",
    "persistence-modifier= persistent    public transient Float Float47",
    "persistence-modifier= persistent  embedded= true   public transient Float Float48",
    "persistence-modifier= persistent  embedded= false   public transient Float Float49",
    "persistence-modifier= transactional    public transient Float Float50",
    "public final Float Float51",
    "public volatile Float Float52",
    "embedded= true   public volatile Float Float53",
    "embedded= false   public volatile Float Float54",
    "persistence-modifier= none    public volatile Float Float55",
    "persistence-modifier= persistent    public volatile Float Float56",
    "persistence-modifier= persistent  embedded= true   public volatile Float Float57",
    "persistence-modifier= persistent  embedded= false   public volatile Float Float58",
    "persistence-modifier= transactional    public volatile Float Float59",
    "public static transient Float Float60",
    "public static final Float Float61",
    "public static volatile Float Float62",
    "public transient final Float Float63",
    "public transient volatile Float Float64",
    "persistence-modifier= none    public transient volatile Float Float65",
    "persistence-modifier= persistent    public transient volatile Float Float66",
    "persistence-modifier= persistent  embedded= true   public transient volatile Float Float67",
    "persistence-modifier= persistent  embedded= false   public transient volatile Float Float68",
    "persistence-modifier= transactional    public transient volatile Float Float69",
    "public static transient final Float Float70",
    "public static transient volatile Float Float71",
    "protected Float Float72",
    "embedded= true   protected Float Float73",
    "embedded= false   protected Float Float74",
    "persistence-modifier= none    protected Float Float75",
    "persistence-modifier= persistent    protected Float Float76",
    "persistence-modifier= persistent  embedded= true   protected Float Float77",
    "persistence-modifier= persistent  embedded= false   protected Float Float78",
    "persistence-modifier= transactional    protected Float Float79",
    "protected static Float Float80",
    "protected transient Float Float81",
    "persistence-modifier= none    protected transient Float Float82",
    "persistence-modifier= persistent    protected transient Float Float83",
    "persistence-modifier= persistent  embedded= true   protected transient Float Float84",
    "persistence-modifier= persistent  embedded= false   protected transient Float Float85",
    "persistence-modifier= transactional    protected transient Float Float86",
    "protected final Float Float87",
    "protected volatile Float Float88",
    "embedded= true   protected volatile Float Float89",
    "embedded= false   protected volatile Float Float90",
    "persistence-modifier= none    protected volatile Float Float91",
    "persistence-modifier= persistent    protected volatile Float Float92",
    "persistence-modifier= persistent  embedded= true   protected volatile Float Float93",
    "persistence-modifier= persistent  embedded= false   protected volatile Float Float94",
    "persistence-modifier= transactional    protected volatile Float Float95",
    "protected static transient Float Float96",
    "protected static final Float Float97",
    "protected static volatile Float Float98",
    "protected transient final Float Float99",
    "protected transient volatile Float Float100",
    "persistence-modifier= none    protected transient volatile Float Float101",
    "persistence-modifier= persistent    protected transient volatile Float Float102",
    "persistence-modifier= persistent  embedded= true   protected transient volatile Float Float103",
    "persistence-modifier= persistent  embedded= false   protected transient volatile Float Float104",
    "persistence-modifier= transactional    protected transient volatile Float Float105",
    "protected static transient final Float Float106",
    "protected static transient volatile Float Float107",
    "Float Float108",
    "embedded= true   Float Float109",
    "embedded= false   Float Float110",
    "persistence-modifier= none    Float Float111",
    "persistence-modifier= persistent    Float Float112",
    "persistence-modifier= persistent  embedded= true   Float Float113",
    "persistence-modifier= persistent  embedded= false   Float Float114",
    "persistence-modifier= transactional    Float Float115",
    "static Float Float116",
    "transient Float Float117",
    "persistence-modifier= none    transient Float Float118",
    "persistence-modifier= persistent    transient Float Float119",
    "persistence-modifier= persistent  embedded= true   transient Float Float120",
    "persistence-modifier= persistent  embedded= false   transient Float Float121",
    "persistence-modifier= transactional    transient Float Float122",
    "final Float Float123",
    "volatile Float Float124",
    "embedded= true   volatile Float Float125",
    "embedded= false   volatile Float Float126",
    "persistence-modifier= none    volatile Float Float127",
    "persistence-modifier= persistent    volatile Float Float128",
    "persistence-modifier= persistent  embedded= true   volatile Float Float129",
    "persistence-modifier= persistent  embedded= false   volatile Float Float130",
    "persistence-modifier= transactional    volatile Float Float131",
    "static transient Float Float132",
    "static final Float Float133",
    "static volatile Float Float134",
    "transient final Float Float135",
    "transient volatile Float Float136",
    "persistence-modifier= none    transient volatile Float Float137",
    "persistence-modifier= persistent    transient volatile Float Float138",
    "persistence-modifier= persistent  embedded= true   transient volatile Float Float139",
    "persistence-modifier= persistent  embedded= false   transient volatile Float Float140",
    "persistence-modifier= transactional    transient volatile Float Float141",
    "static transient final Float Float142",
    "static transient volatile Float Float143"
  };

  public int getLength() {
    return fieldSpecs.length;
  }

  public Float get(int index) {
    switch (index) {
      case (0):
        return Float0;
      case (1):
        return Float1;
      case (2):
        return Float2;
      case (3):
        return Float3;
      case (4):
        return Float4;
      case (5):
        return Float5;
      case (6):
        return Float6;
      case (7):
        return Float7;
      case (8):
        return Float8;
      case (9):
        return Float9;
      case (10):
        return Float10;
      case (11):
        return Float11;
      case (12):
        return Float12;
      case (13):
        return Float13;
      case (14):
        return Float14;
      case (15):
        return Float15;
      case (16):
        return Float16;
      case (17):
        return Float17;
      case (18):
        return Float18;
      case (19):
        return Float19;
      case (20):
        return Float20;
      case (21):
        return Float21;
      case (22):
        return Float22;
      case (23):
        return Float23;
      case (24):
        return Float24;
      case (25):
        return Float25;
      case (26):
        return Float26;
      case (27):
        return Float27;
      case (28):
        return Float28;
      case (29):
        return Float29;
      case (30):
        return Float30;
      case (31):
        return Float31;
      case (32):
        return Float32;
      case (33):
        return Float33;
      case (34):
        return Float34;
      case (35):
        return Float35;
      case (36):
        return Float36;
      case (37):
        return Float37;
      case (38):
        return Float38;
      case (39):
        return Float39;
      case (40):
        return Float40;
      case (41):
        return Float41;
      case (42):
        return Float42;
      case (43):
        return Float43;
      case (44):
        return Float44;
      case (45):
        return Float45;
      case (46):
        return Float46;
      case (47):
        return Float47;
      case (48):
        return Float48;
      case (49):
        return Float49;
      case (50):
        return Float50;
      case (51):
        return Float51;
      case (52):
        return Float52;
      case (53):
        return Float53;
      case (54):
        return Float54;
      case (55):
        return Float55;
      case (56):
        return Float56;
      case (57):
        return Float57;
      case (58):
        return Float58;
      case (59):
        return Float59;
      case (60):
        return Float60;
      case (61):
        return Float61;
      case (62):
        return Float62;
      case (63):
        return Float63;
      case (64):
        return Float64;
      case (65):
        return Float65;
      case (66):
        return Float66;
      case (67):
        return Float67;
      case (68):
        return Float68;
      case (69):
        return Float69;
      case (70):
        return Float70;
      case (71):
        return Float71;
      case (72):
        return Float72;
      case (73):
        return Float73;
      case (74):
        return Float74;
      case (75):
        return Float75;
      case (76):
        return Float76;
      case (77):
        return Float77;
      case (78):
        return Float78;
      case (79):
        return Float79;
      case (80):
        return Float80;
      case (81):
        return Float81;
      case (82):
        return Float82;
      case (83):
        return Float83;
      case (84):
        return Float84;
      case (85):
        return Float85;
      case (86):
        return Float86;
      case (87):
        return Float87;
      case (88):
        return Float88;
      case (89):
        return Float89;
      case (90):
        return Float90;
      case (91):
        return Float91;
      case (92):
        return Float92;
      case (93):
        return Float93;
      case (94):
        return Float94;
      case (95):
        return Float95;
      case (96):
        return Float96;
      case (97):
        return Float97;
      case (98):
        return Float98;
      case (99):
        return Float99;
      case (100):
        return Float100;
      case (101):
        return Float101;
      case (102):
        return Float102;
      case (103):
        return Float103;
      case (104):
        return Float104;
      case (105):
        return Float105;
      case (106):
        return Float106;
      case (107):
        return Float107;
      case (108):
        return Float108;
      case (109):
        return Float109;
      case (110):
        return Float110;
      case (111):
        return Float111;
      case (112):
        return Float112;
      case (113):
        return Float113;
      case (114):
        return Float114;
      case (115):
        return Float115;
      case (116):
        return Float116;
      case (117):
        return Float117;
      case (118):
        return Float118;
      case (119):
        return Float119;
      case (120):
        return Float120;
      case (121):
        return Float121;
      case (122):
        return Float122;
      case (123):
        return Float123;
      case (124):
        return Float124;
      case (125):
        return Float125;
      case (126):
        return Float126;
      case (127):
        return Float127;
      case (128):
        return Float128;
      case (129):
        return Float129;
      case (130):
        return Float130;
      case (131):
        return Float131;
      case (132):
        return Float132;
      case (133):
        return Float133;
      case (134):
        return Float134;
      case (135):
        return Float135;
      case (136):
        return Float136;
      case (137):
        return Float137;
      case (138):
        return Float138;
      case (139):
        return Float139;
      case (140):
        return Float140;
      case (141):
        return Float141;
      case (142):
        return Float142;
      case (143):
        return Float143;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  public boolean set(int index, Float value) {
    if (fieldSpecs[index].indexOf("final") != -1) return false;
    switch (index) {
      case (0):
        Float0 = value;
        break;
      case (1):
        Float1 = value;
        break;
      case (2):
        Float2 = value;
        break;
      case (3):
        Float3 = value;
        break;
      case (4):
        Float4 = value;
        break;
      case (5):
        Float5 = value;
        break;
      case (6):
        Float6 = value;
        break;
      case (7):
        Float7 = value;
        break;
      case (8):
        Float8 = value;
        break;
      case (9):
        Float9 = value;
        break;
      case (10):
        Float10 = value;
        break;
      case (11):
        Float11 = value;
        break;
      case (12):
        Float12 = value;
        break;
      case (13):
        Float13 = value;
        break;
      case (14):
        Float14 = value;
        break;
      case (16):
        Float16 = value;
        break;
      case (17):
        Float17 = value;
        break;
      case (18):
        Float18 = value;
        break;
      case (19):
        Float19 = value;
        break;
      case (20):
        Float20 = value;
        break;
      case (21):
        Float21 = value;
        break;
      case (22):
        Float22 = value;
        break;
      case (23):
        Float23 = value;
        break;
      case (24):
        Float24 = value;
        break;
      case (26):
        Float26 = value;
        break;
      case (28):
        Float28 = value;
        break;
      case (29):
        Float29 = value;
        break;
      case (30):
        Float30 = value;
        break;
      case (31):
        Float31 = value;
        break;
      case (32):
        Float32 = value;
        break;
      case (33):
        Float33 = value;
        break;
      case (35):
        Float35 = value;
        break;
      case (36):
        Float36 = value;
        break;
      case (37):
        Float37 = value;
        break;
      case (38):
        Float38 = value;
        break;
      case (39):
        Float39 = value;
        break;
      case (40):
        Float40 = value;
        break;
      case (41):
        Float41 = value;
        break;
      case (42):
        Float42 = value;
        break;
      case (43):
        Float43 = value;
        break;
      case (44):
        Float44 = value;
        break;
      case (45):
        Float45 = value;
        break;
      case (46):
        Float46 = value;
        break;
      case (47):
        Float47 = value;
        break;
      case (48):
        Float48 = value;
        break;
      case (49):
        Float49 = value;
        break;
      case (50):
        Float50 = value;
        break;
      case (52):
        Float52 = value;
        break;
      case (53):
        Float53 = value;
        break;
      case (54):
        Float54 = value;
        break;
      case (55):
        Float55 = value;
        break;
      case (56):
        Float56 = value;
        break;
      case (57):
        Float57 = value;
        break;
      case (58):
        Float58 = value;
        break;
      case (59):
        Float59 = value;
        break;
      case (60):
        Float60 = value;
        break;
      case (62):
        Float62 = value;
        break;
      case (64):
        Float64 = value;
        break;
      case (65):
        Float65 = value;
        break;
      case (66):
        Float66 = value;
        break;
      case (67):
        Float67 = value;
        break;
      case (68):
        Float68 = value;
        break;
      case (69):
        Float69 = value;
        break;
      case (71):
        Float71 = value;
        break;
      case (72):
        Float72 = value;
        break;
      case (73):
        Float73 = value;
        break;
      case (74):
        Float74 = value;
        break;
      case (75):
        Float75 = value;
        break;
      case (76):
        Float76 = value;
        break;
      case (77):
        Float77 = value;
        break;
      case (78):
        Float78 = value;
        break;
      case (79):
        Float79 = value;
        break;
      case (80):
        Float80 = value;
        break;
      case (81):
        Float81 = value;
        break;
      case (82):
        Float82 = value;
        break;
      case (83):
        Float83 = value;
        break;
      case (84):
        Float84 = value;
        break;
      case (85):
        Float85 = value;
        break;
      case (86):
        Float86 = value;
        break;
      case (88):
        Float88 = value;
        break;
      case (89):
        Float89 = value;
        break;
      case (90):
        Float90 = value;
        break;
      case (91):
        Float91 = value;
        break;
      case (92):
        Float92 = value;
        break;
      case (93):
        Float93 = value;
        break;
      case (94):
        Float94 = value;
        break;
      case (95):
        Float95 = value;
        break;
      case (96):
        Float96 = value;
        break;
      case (98):
        Float98 = value;
        break;
      case (100):
        Float100 = value;
        break;
      case (101):
        Float101 = value;
        break;
      case (102):
        Float102 = value;
        break;
      case (103):
        Float103 = value;
        break;
      case (104):
        Float104 = value;
        break;
      case (105):
        Float105 = value;
        break;
      case (107):
        Float107 = value;
        break;
      case (108):
        Float108 = value;
        break;
      case (109):
        Float109 = value;
        break;
      case (110):
        Float110 = value;
        break;
      case (111):
        Float111 = value;
        break;
      case (112):
        Float112 = value;
        break;
      case (113):
        Float113 = value;
        break;
      case (114):
        Float114 = value;
        break;
      case (115):
        Float115 = value;
        break;
      case (116):
        Float116 = value;
        break;
      case (117):
        Float117 = value;
        break;
      case (118):
        Float118 = value;
        break;
      case (119):
        Float119 = value;
        break;
      case (120):
        Float120 = value;
        break;
      case (121):
        Float121 = value;
        break;
      case (122):
        Float122 = value;
        break;
      case (124):
        Float124 = value;
        break;
      case (125):
        Float125 = value;
        break;
      case (126):
        Float126 = value;
        break;
      case (127):
        Float127 = value;
        break;
      case (128):
        Float128 = value;
        break;
      case (129):
        Float129 = value;
        break;
      case (130):
        Float130 = value;
        break;
      case (131):
        Float131 = value;
        break;
      case (132):
        Float132 = value;
        break;
      case (134):
        Float134 = value;
        break;
      case (136):
        Float136 = value;
        break;
      case (137):
        Float137 = value;
        break;
      case (138):
        Float138 = value;
        break;
      case (139):
        Float139 = value;
        break;
      case (140):
        Float140 = value;
        break;
      case (141):
        Float141 = value;
        break;
      case (143):
        Float143 = value;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
    return true;
  }

  public static class Oid implements Serializable {
    private static final long serialVersionUID = 1L;

    public int identifier;

    public Oid() {}

    public Oid(String s) {
      identifier = Integer.parseInt(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + identifier;
    }

    public int hashCode() {
      return identifier;
    }

    public boolean equals(Object other) {
      if (other != null && (other instanceof Oid)) {
        Oid k = (Oid) other;
        return k.identifier == this.identifier;
      }
      return false;
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }
  }
}
