Test scenarios with serializable.

(1) A pc class has no persistence-capable superclass, implements 
java.io.Serializable, but does not provide an implementation of method 
writeObject or writeReplace.
 
The enhancer should add the following methods to the pc class:
- protected final void jdoPreSerialize() {
      final javax.jdo.spi.StateManager sm = this.jdoStateManager;
      if (sm != null) { sm.preSerialize(this); }
   }
- private void writeObject(java.io.ObjectOutputStream out)
     throws java.io.IOException {
     jdoPreSerialize();
     out.defaultWriteObject();
   }

(2A) A pc class has no persistence-capable superclass, implements 
java.io.Serializable and provides an implementation of method writeObject.

The enhancer should add a method jdoPreSerialize (code see above) and
should add a call of method jdoPreSerialize() to the existing
method writeObject. It needs to be the first statement of writeObject.

(2B) A pc class has no persistence-capable superclass, implements 
java.io.Serializable and provides an implementation of method writeReplace.

The enhancer should add a method jdoPreSerialize (code see above) and
should add a call of method jdoPreSerialize() to the existing
method writeReplace. It needs to be the first statement of writeReplace.

(3) There is the following inheritance hierarchy:
PCSuper <- Transient <- PCSub
- PCSuper and PCSub are pc classes, Transient is a non-pc class.
- PCSuper does not implement java.io.Serializable, but Transient does.
  PCSub automatically implement java.io.Serializable, because its super
  class implements the interface.
- PCSub does not provide an implementation of method writeObject.

The enhancer should add a method jdoPreSerialize (code see above) to
PCSuper and should add a method writeObject (code see above) to PCSub.

(4A) The same inheritance hierarchy as in (3) the only difference is that
PCSub provides an implementation of method writeObject.

The enhancer should add a method jdoPreSerialize (code see above) to
PCSuper and should add a call of method jdoPreSerialize() to the
existing method writeObject. It needs to be the first statement of
writeObject.

(4B) The same inheritance hierarchy as in (3) the only difference is that
PCSub provides an implementation of method writeReplace.

The enhancer should add a method jdoPreSerialize (code see above) and
should add a call of method jdoPreSerialize() to the existing
method writeReplace. It needs to be the first statement of writeReplace.

