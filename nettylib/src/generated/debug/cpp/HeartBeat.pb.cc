// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: HeartBeat.proto

#define INTERNAL_SUPPRESS_PROTOBUF_FIELD_DEPRECATION
#include "HeartBeat.pb.h"

#include <algorithm>

#include <google/protobuf/stubs/common.h>
#include <google/protobuf/stubs/port.h>
#include <google/protobuf/stubs/once.h>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/wire_format_lite_inl.h>
#include <google/protobuf/descriptor.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/reflection_ops.h>
#include <google/protobuf/wire_format.h>
// @@protoc_insertion_point(includes)

namespace {

const ::google::protobuf::Descriptor* HeartBeat_descriptor_ = NULL;
const ::google::protobuf::internal::GeneratedMessageReflection*
  HeartBeat_reflection_ = NULL;
const ::google::protobuf::Descriptor* ProtoEvent_descriptor_ = NULL;
const ::google::protobuf::internal::GeneratedMessageReflection*
  ProtoEvent_reflection_ = NULL;

}  // namespace


void protobuf_AssignDesc_HeartBeat_2eproto() GOOGLE_ATTRIBUTE_COLD;
void protobuf_AssignDesc_HeartBeat_2eproto() {
  protobuf_AddDesc_HeartBeat_2eproto();
  const ::google::protobuf::FileDescriptor* file =
    ::google::protobuf::DescriptorPool::generated_pool()->FindFileByName(
      "HeartBeat.proto");
  GOOGLE_CHECK(file != NULL);
  HeartBeat_descriptor_ = file->message_type(0);
  static const int HeartBeat_offsets_[1] = {
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(HeartBeat, time_),
  };
  HeartBeat_reflection_ =
    ::google::protobuf::internal::GeneratedMessageReflection::NewGeneratedMessageReflection(
      HeartBeat_descriptor_,
      HeartBeat::internal_default_instance(),
      HeartBeat_offsets_,
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(HeartBeat, _has_bits_),
      -1,
      -1,
      sizeof(HeartBeat),
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(HeartBeat, _internal_metadata_));
  ProtoEvent_descriptor_ = file->message_type(1);
  static const int ProtoEvent_offsets_[6] = {
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, time_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, event_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, message_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, what1_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, what2_),
    GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, json_),
  };
  ProtoEvent_reflection_ =
    ::google::protobuf::internal::GeneratedMessageReflection::NewGeneratedMessageReflection(
      ProtoEvent_descriptor_,
      ProtoEvent::internal_default_instance(),
      ProtoEvent_offsets_,
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, _has_bits_),
      -1,
      -1,
      sizeof(ProtoEvent),
      GOOGLE_PROTOBUF_GENERATED_MESSAGE_FIELD_OFFSET(ProtoEvent, _internal_metadata_));
}

namespace {

GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AssignDescriptors_once_);
void protobuf_AssignDescriptorsOnce() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AssignDescriptors_once_,
                 &protobuf_AssignDesc_HeartBeat_2eproto);
}

void protobuf_RegisterTypes(const ::std::string&) GOOGLE_ATTRIBUTE_COLD;
void protobuf_RegisterTypes(const ::std::string&) {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedMessage(
      HeartBeat_descriptor_, HeartBeat::internal_default_instance());
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedMessage(
      ProtoEvent_descriptor_, ProtoEvent::internal_default_instance());
}

}  // namespace

void protobuf_ShutdownFile_HeartBeat_2eproto() {
  HeartBeat_default_instance_.Shutdown();
  delete HeartBeat_reflection_;
  ProtoEvent_default_instance_.Shutdown();
  delete ProtoEvent_reflection_;
}

void protobuf_InitDefaults_HeartBeat_2eproto_impl() {
  GOOGLE_PROTOBUF_VERIFY_VERSION;

  HeartBeat_default_instance_.DefaultConstruct();
  ::google::protobuf::internal::GetEmptyString();
  ProtoEvent_default_instance_.DefaultConstruct();
  HeartBeat_default_instance_.get_mutable()->InitAsDefaultInstance();
  ProtoEvent_default_instance_.get_mutable()->InitAsDefaultInstance();
}

GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_InitDefaults_HeartBeat_2eproto_once_);
void protobuf_InitDefaults_HeartBeat_2eproto() {
  ::google::protobuf::GoogleOnceInit(&protobuf_InitDefaults_HeartBeat_2eproto_once_,
                 &protobuf_InitDefaults_HeartBeat_2eproto_impl);
}
void protobuf_AddDesc_HeartBeat_2eproto_impl() {
  GOOGLE_PROTOBUF_VERIFY_VERSION;

  protobuf_InitDefaults_HeartBeat_2eproto();
  ::google::protobuf::DescriptorPool::InternalAddGeneratedFile(
    "\n\017HeartBeat.proto\"\031\n\tHeartBeat\022\014\n\004time\030\001"
    " \001(\003\"f\n\nProtoEvent\022\014\n\004time\030\001 \001(\003\022\r\n\005even"
    "t\030\002 \001(\003\022\017\n\007message\030\003 \001(\t\022\r\n\005what1\030\004 \001(\003\022"
    "\r\n\005what2\030\005 \001(\003\022\014\n\004json\030\006 \001(\tB+\n\032com.exam"
    "ple.nettylib.protoB\rHeartBeatData", 193);
  ::google::protobuf::MessageFactory::InternalRegisterGeneratedFile(
    "HeartBeat.proto", &protobuf_RegisterTypes);
  ::google::protobuf::internal::OnShutdown(&protobuf_ShutdownFile_HeartBeat_2eproto);
}

GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AddDesc_HeartBeat_2eproto_once_);
void protobuf_AddDesc_HeartBeat_2eproto() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AddDesc_HeartBeat_2eproto_once_,
                 &protobuf_AddDesc_HeartBeat_2eproto_impl);
}
// Force AddDescriptors() to be called at static initialization time.
struct StaticDescriptorInitializer_HeartBeat_2eproto {
  StaticDescriptorInitializer_HeartBeat_2eproto() {
    protobuf_AddDesc_HeartBeat_2eproto();
  }
} static_descriptor_initializer_HeartBeat_2eproto_;

namespace {

static void MergeFromFail(int line) GOOGLE_ATTRIBUTE_COLD GOOGLE_ATTRIBUTE_NORETURN;
static void MergeFromFail(int line) {
  ::google::protobuf::internal::MergeFromFail(__FILE__, line);
}

}  // namespace


// ===================================================================

#if !defined(_MSC_VER) || _MSC_VER >= 1900
const int HeartBeat::kTimeFieldNumber;
#endif  // !defined(_MSC_VER) || _MSC_VER >= 1900

HeartBeat::HeartBeat()
  : ::google::protobuf::Message(), _internal_metadata_(NULL) {
  if (this != internal_default_instance()) protobuf_InitDefaults_HeartBeat_2eproto();
  SharedCtor();
  // @@protoc_insertion_point(constructor:HeartBeat)
}

void HeartBeat::InitAsDefaultInstance() {
}

HeartBeat::HeartBeat(const HeartBeat& from)
  : ::google::protobuf::Message(),
    _internal_metadata_(NULL) {
  SharedCtor();
  UnsafeMergeFrom(from);
  // @@protoc_insertion_point(copy_constructor:HeartBeat)
}

void HeartBeat::SharedCtor() {
  _cached_size_ = 0;
  time_ = GOOGLE_LONGLONG(0);
}

HeartBeat::~HeartBeat() {
  // @@protoc_insertion_point(destructor:HeartBeat)
  SharedDtor();
}

void HeartBeat::SharedDtor() {
}

void HeartBeat::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const ::google::protobuf::Descriptor* HeartBeat::descriptor() {
  protobuf_AssignDescriptorsOnce();
  return HeartBeat_descriptor_;
}

const HeartBeat& HeartBeat::default_instance() {
  protobuf_InitDefaults_HeartBeat_2eproto();
  return *internal_default_instance();
}

::google::protobuf::internal::ExplicitlyConstructed<HeartBeat> HeartBeat_default_instance_;

HeartBeat* HeartBeat::New(::google::protobuf::Arena* arena) const {
  HeartBeat* n = new HeartBeat;
  if (arena != NULL) {
    arena->Own(n);
  }
  return n;
}

void HeartBeat::Clear() {
// @@protoc_insertion_point(message_clear_start:HeartBeat)
  time_ = GOOGLE_LONGLONG(0);
  _has_bits_.Clear();
  if (_internal_metadata_.have_unknown_fields()) {
    mutable_unknown_fields()->Clear();
  }
}

bool HeartBeat::MergePartialFromCodedStream(
    ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!GOOGLE_PREDICT_TRUE(EXPRESSION)) goto failure
  ::google::protobuf::uint32 tag;
  // @@protoc_insertion_point(parse_start:HeartBeat)
  for (;;) {
    ::std::pair< ::google::protobuf::uint32, bool> p = input->ReadTagWithCutoff(127);
    tag = p.first;
    if (!p.second) goto handle_unusual;
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
      // optional int64 time = 1;
      case 1: {
        if (tag == 8) {
          set_has_time();
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &time_)));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectAtEnd()) goto success;
        break;
      }

      default: {
      handle_unusual:
        if (tag == 0 ||
            ::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_END_GROUP) {
          goto success;
        }
        DO_(::google::protobuf::internal::WireFormat::SkipField(
              input, tag, mutable_unknown_fields()));
        break;
      }
    }
  }
success:
  // @@protoc_insertion_point(parse_success:HeartBeat)
  return true;
failure:
  // @@protoc_insertion_point(parse_failure:HeartBeat)
  return false;
#undef DO_
}

void HeartBeat::SerializeWithCachedSizes(
    ::google::protobuf::io::CodedOutputStream* output) const {
  // @@protoc_insertion_point(serialize_start:HeartBeat)
  // optional int64 time = 1;
  if (has_time()) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(1, this->time(), output);
  }

  if (_internal_metadata_.have_unknown_fields()) {
    ::google::protobuf::internal::WireFormat::SerializeUnknownFields(
        unknown_fields(), output);
  }
  // @@protoc_insertion_point(serialize_end:HeartBeat)
}

::google::protobuf::uint8* HeartBeat::InternalSerializeWithCachedSizesToArray(
    bool deterministic, ::google::protobuf::uint8* target) const {
  (void)deterministic; // Unused
  // @@protoc_insertion_point(serialize_to_array_start:HeartBeat)
  // optional int64 time = 1;
  if (has_time()) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(1, this->time(), target);
  }

  if (_internal_metadata_.have_unknown_fields()) {
    target = ::google::protobuf::internal::WireFormat::SerializeUnknownFieldsToArray(
        unknown_fields(), target);
  }
  // @@protoc_insertion_point(serialize_to_array_end:HeartBeat)
  return target;
}

size_t HeartBeat::ByteSizeLong() const {
// @@protoc_insertion_point(message_byte_size_start:HeartBeat)
  size_t total_size = 0;

  // optional int64 time = 1;
  if (has_time()) {
    total_size += 1 +
      ::google::protobuf::internal::WireFormatLite::Int64Size(
        this->time());
  }

  if (_internal_metadata_.have_unknown_fields()) {
    total_size +=
      ::google::protobuf::internal::WireFormat::ComputeUnknownFieldsSize(
        unknown_fields());
  }
  int cached_size = ::google::protobuf::internal::ToCachedSize(total_size);
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = cached_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void HeartBeat::MergeFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_merge_from_start:HeartBeat)
  if (GOOGLE_PREDICT_FALSE(&from == this)) MergeFromFail(__LINE__);
  const HeartBeat* source =
      ::google::protobuf::internal::DynamicCastToGenerated<const HeartBeat>(
          &from);
  if (source == NULL) {
  // @@protoc_insertion_point(generalized_merge_from_cast_fail:HeartBeat)
    ::google::protobuf::internal::ReflectionOps::Merge(from, this);
  } else {
  // @@protoc_insertion_point(generalized_merge_from_cast_success:HeartBeat)
    UnsafeMergeFrom(*source);
  }
}

void HeartBeat::MergeFrom(const HeartBeat& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:HeartBeat)
  if (GOOGLE_PREDICT_TRUE(&from != this)) {
    UnsafeMergeFrom(from);
  } else {
    MergeFromFail(__LINE__);
  }
}

void HeartBeat::UnsafeMergeFrom(const HeartBeat& from) {
  GOOGLE_DCHECK(&from != this);
  if (from._has_bits_[0 / 32] & (0xffu << (0 % 32))) {
    if (from.has_time()) {
      set_time(from.time());
    }
  }
  if (from._internal_metadata_.have_unknown_fields()) {
    ::google::protobuf::UnknownFieldSet::MergeToInternalMetdata(
      from.unknown_fields(), &_internal_metadata_);
  }
}

void HeartBeat::CopyFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_copy_from_start:HeartBeat)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

void HeartBeat::CopyFrom(const HeartBeat& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:HeartBeat)
  if (&from == this) return;
  Clear();
  UnsafeMergeFrom(from);
}

bool HeartBeat::IsInitialized() const {

  return true;
}

void HeartBeat::Swap(HeartBeat* other) {
  if (other == this) return;
  InternalSwap(other);
}
void HeartBeat::InternalSwap(HeartBeat* other) {
  std::swap(time_, other->time_);
  std::swap(_has_bits_[0], other->_has_bits_[0]);
  _internal_metadata_.Swap(&other->_internal_metadata_);
  std::swap(_cached_size_, other->_cached_size_);
}

::google::protobuf::Metadata HeartBeat::GetMetadata() const {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::Metadata metadata;
  metadata.descriptor = HeartBeat_descriptor_;
  metadata.reflection = HeartBeat_reflection_;
  return metadata;
}

#if PROTOBUF_INLINE_NOT_IN_HEADERS
// HeartBeat

// optional int64 time = 1;
bool HeartBeat::has_time() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
void HeartBeat::set_has_time() {
  _has_bits_[0] |= 0x00000001u;
}
void HeartBeat::clear_has_time() {
  _has_bits_[0] &= ~0x00000001u;
}
void HeartBeat::clear_time() {
  time_ = GOOGLE_LONGLONG(0);
  clear_has_time();
}
::google::protobuf::int64 HeartBeat::time() const {
  // @@protoc_insertion_point(field_get:HeartBeat.time)
  return time_;
}
void HeartBeat::set_time(::google::protobuf::int64 value) {
  set_has_time();
  time_ = value;
  // @@protoc_insertion_point(field_set:HeartBeat.time)
}

inline const HeartBeat* HeartBeat::internal_default_instance() {
  return &HeartBeat_default_instance_.get();
}
#endif  // PROTOBUF_INLINE_NOT_IN_HEADERS

// ===================================================================

#if !defined(_MSC_VER) || _MSC_VER >= 1900
const int ProtoEvent::kTimeFieldNumber;
const int ProtoEvent::kEventFieldNumber;
const int ProtoEvent::kMessageFieldNumber;
const int ProtoEvent::kWhat1FieldNumber;
const int ProtoEvent::kWhat2FieldNumber;
const int ProtoEvent::kJsonFieldNumber;
#endif  // !defined(_MSC_VER) || _MSC_VER >= 1900

ProtoEvent::ProtoEvent()
  : ::google::protobuf::Message(), _internal_metadata_(NULL) {
  if (this != internal_default_instance()) protobuf_InitDefaults_HeartBeat_2eproto();
  SharedCtor();
  // @@protoc_insertion_point(constructor:ProtoEvent)
}

void ProtoEvent::InitAsDefaultInstance() {
}

ProtoEvent::ProtoEvent(const ProtoEvent& from)
  : ::google::protobuf::Message(),
    _internal_metadata_(NULL) {
  SharedCtor();
  UnsafeMergeFrom(from);
  // @@protoc_insertion_point(copy_constructor:ProtoEvent)
}

void ProtoEvent::SharedCtor() {
  _cached_size_ = 0;
  message_.UnsafeSetDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  json_.UnsafeSetDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  ::memset(&time_, 0, reinterpret_cast<char*>(&what2_) -
    reinterpret_cast<char*>(&time_) + sizeof(what2_));
}

ProtoEvent::~ProtoEvent() {
  // @@protoc_insertion_point(destructor:ProtoEvent)
  SharedDtor();
}

void ProtoEvent::SharedDtor() {
  message_.DestroyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  json_.DestroyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}

void ProtoEvent::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const ::google::protobuf::Descriptor* ProtoEvent::descriptor() {
  protobuf_AssignDescriptorsOnce();
  return ProtoEvent_descriptor_;
}

const ProtoEvent& ProtoEvent::default_instance() {
  protobuf_InitDefaults_HeartBeat_2eproto();
  return *internal_default_instance();
}

::google::protobuf::internal::ExplicitlyConstructed<ProtoEvent> ProtoEvent_default_instance_;

ProtoEvent* ProtoEvent::New(::google::protobuf::Arena* arena) const {
  ProtoEvent* n = new ProtoEvent;
  if (arena != NULL) {
    arena->Own(n);
  }
  return n;
}

void ProtoEvent::Clear() {
// @@protoc_insertion_point(message_clear_start:ProtoEvent)
#if defined(__clang__)
#define ZR_HELPER_(f) \
  _Pragma("clang diagnostic push") \
  _Pragma("clang diagnostic ignored \"-Winvalid-offsetof\"") \
  __builtin_offsetof(ProtoEvent, f) \
  _Pragma("clang diagnostic pop")
#else
#define ZR_HELPER_(f) reinterpret_cast<char*>(\
  &reinterpret_cast<ProtoEvent*>(16)->f)
#endif

#define ZR_(first, last) do {\
  ::memset(&(first), 0,\
           ZR_HELPER_(last) - ZR_HELPER_(first) + sizeof(last));\
} while (0)

  if (_has_bits_[0 / 32] & 63u) {
    ZR_(time_, what2_);
    if (has_message()) {
      message_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
    }
    if (has_json()) {
      json_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
    }
  }

#undef ZR_HELPER_
#undef ZR_

  _has_bits_.Clear();
  if (_internal_metadata_.have_unknown_fields()) {
    mutable_unknown_fields()->Clear();
  }
}

bool ProtoEvent::MergePartialFromCodedStream(
    ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!GOOGLE_PREDICT_TRUE(EXPRESSION)) goto failure
  ::google::protobuf::uint32 tag;
  // @@protoc_insertion_point(parse_start:ProtoEvent)
  for (;;) {
    ::std::pair< ::google::protobuf::uint32, bool> p = input->ReadTagWithCutoff(127);
    tag = p.first;
    if (!p.second) goto handle_unusual;
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
      // optional int64 time = 1;
      case 1: {
        if (tag == 8) {
          set_has_time();
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &time_)));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(16)) goto parse_event;
        break;
      }

      // optional int64 event = 2;
      case 2: {
        if (tag == 16) {
         parse_event:
          set_has_event();
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &event_)));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(26)) goto parse_message;
        break;
      }

      // optional string message = 3;
      case 3: {
        if (tag == 26) {
         parse_message:
          DO_(::google::protobuf::internal::WireFormatLite::ReadString(
                input, this->mutable_message()));
          ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
            this->message().data(), this->message().length(),
            ::google::protobuf::internal::WireFormat::PARSE,
            "ProtoEvent.message");
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(32)) goto parse_what1;
        break;
      }

      // optional int64 what1 = 4;
      case 4: {
        if (tag == 32) {
         parse_what1:
          set_has_what1();
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &what1_)));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(40)) goto parse_what2;
        break;
      }

      // optional int64 what2 = 5;
      case 5: {
        if (tag == 40) {
         parse_what2:
          set_has_what2();
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int64, ::google::protobuf::internal::WireFormatLite::TYPE_INT64>(
                 input, &what2_)));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(50)) goto parse_json;
        break;
      }

      // optional string json = 6;
      case 6: {
        if (tag == 50) {
         parse_json:
          DO_(::google::protobuf::internal::WireFormatLite::ReadString(
                input, this->mutable_json()));
          ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
            this->json().data(), this->json().length(),
            ::google::protobuf::internal::WireFormat::PARSE,
            "ProtoEvent.json");
        } else {
          goto handle_unusual;
        }
        if (input->ExpectAtEnd()) goto success;
        break;
      }

      default: {
      handle_unusual:
        if (tag == 0 ||
            ::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_END_GROUP) {
          goto success;
        }
        DO_(::google::protobuf::internal::WireFormat::SkipField(
              input, tag, mutable_unknown_fields()));
        break;
      }
    }
  }
success:
  // @@protoc_insertion_point(parse_success:ProtoEvent)
  return true;
failure:
  // @@protoc_insertion_point(parse_failure:ProtoEvent)
  return false;
#undef DO_
}

void ProtoEvent::SerializeWithCachedSizes(
    ::google::protobuf::io::CodedOutputStream* output) const {
  // @@protoc_insertion_point(serialize_start:ProtoEvent)
  // optional int64 time = 1;
  if (has_time()) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(1, this->time(), output);
  }

  // optional int64 event = 2;
  if (has_event()) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(2, this->event(), output);
  }

  // optional string message = 3;
  if (has_message()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
      this->message().data(), this->message().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE,
      "ProtoEvent.message");
    ::google::protobuf::internal::WireFormatLite::WriteStringMaybeAliased(
      3, this->message(), output);
  }

  // optional int64 what1 = 4;
  if (has_what1()) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(4, this->what1(), output);
  }

  // optional int64 what2 = 5;
  if (has_what2()) {
    ::google::protobuf::internal::WireFormatLite::WriteInt64(5, this->what2(), output);
  }

  // optional string json = 6;
  if (has_json()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
      this->json().data(), this->json().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE,
      "ProtoEvent.json");
    ::google::protobuf::internal::WireFormatLite::WriteStringMaybeAliased(
      6, this->json(), output);
  }

  if (_internal_metadata_.have_unknown_fields()) {
    ::google::protobuf::internal::WireFormat::SerializeUnknownFields(
        unknown_fields(), output);
  }
  // @@protoc_insertion_point(serialize_end:ProtoEvent)
}

::google::protobuf::uint8* ProtoEvent::InternalSerializeWithCachedSizesToArray(
    bool deterministic, ::google::protobuf::uint8* target) const {
  (void)deterministic; // Unused
  // @@protoc_insertion_point(serialize_to_array_start:ProtoEvent)
  // optional int64 time = 1;
  if (has_time()) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(1, this->time(), target);
  }

  // optional int64 event = 2;
  if (has_event()) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(2, this->event(), target);
  }

  // optional string message = 3;
  if (has_message()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
      this->message().data(), this->message().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE,
      "ProtoEvent.message");
    target =
      ::google::protobuf::internal::WireFormatLite::WriteStringToArray(
        3, this->message(), target);
  }

  // optional int64 what1 = 4;
  if (has_what1()) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(4, this->what1(), target);
  }

  // optional int64 what2 = 5;
  if (has_what2()) {
    target = ::google::protobuf::internal::WireFormatLite::WriteInt64ToArray(5, this->what2(), target);
  }

  // optional string json = 6;
  if (has_json()) {
    ::google::protobuf::internal::WireFormat::VerifyUTF8StringNamedField(
      this->json().data(), this->json().length(),
      ::google::protobuf::internal::WireFormat::SERIALIZE,
      "ProtoEvent.json");
    target =
      ::google::protobuf::internal::WireFormatLite::WriteStringToArray(
        6, this->json(), target);
  }

  if (_internal_metadata_.have_unknown_fields()) {
    target = ::google::protobuf::internal::WireFormat::SerializeUnknownFieldsToArray(
        unknown_fields(), target);
  }
  // @@protoc_insertion_point(serialize_to_array_end:ProtoEvent)
  return target;
}

size_t ProtoEvent::ByteSizeLong() const {
// @@protoc_insertion_point(message_byte_size_start:ProtoEvent)
  size_t total_size = 0;

  if (_has_bits_[0 / 32] & 63u) {
    // optional int64 time = 1;
    if (has_time()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::Int64Size(
          this->time());
    }

    // optional int64 event = 2;
    if (has_event()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::Int64Size(
          this->event());
    }

    // optional string message = 3;
    if (has_message()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::StringSize(
          this->message());
    }

    // optional int64 what1 = 4;
    if (has_what1()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::Int64Size(
          this->what1());
    }

    // optional int64 what2 = 5;
    if (has_what2()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::Int64Size(
          this->what2());
    }

    // optional string json = 6;
    if (has_json()) {
      total_size += 1 +
        ::google::protobuf::internal::WireFormatLite::StringSize(
          this->json());
    }

  }
  if (_internal_metadata_.have_unknown_fields()) {
    total_size +=
      ::google::protobuf::internal::WireFormat::ComputeUnknownFieldsSize(
        unknown_fields());
  }
  int cached_size = ::google::protobuf::internal::ToCachedSize(total_size);
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = cached_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void ProtoEvent::MergeFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_merge_from_start:ProtoEvent)
  if (GOOGLE_PREDICT_FALSE(&from == this)) MergeFromFail(__LINE__);
  const ProtoEvent* source =
      ::google::protobuf::internal::DynamicCastToGenerated<const ProtoEvent>(
          &from);
  if (source == NULL) {
  // @@protoc_insertion_point(generalized_merge_from_cast_fail:ProtoEvent)
    ::google::protobuf::internal::ReflectionOps::Merge(from, this);
  } else {
  // @@protoc_insertion_point(generalized_merge_from_cast_success:ProtoEvent)
    UnsafeMergeFrom(*source);
  }
}

void ProtoEvent::MergeFrom(const ProtoEvent& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:ProtoEvent)
  if (GOOGLE_PREDICT_TRUE(&from != this)) {
    UnsafeMergeFrom(from);
  } else {
    MergeFromFail(__LINE__);
  }
}

void ProtoEvent::UnsafeMergeFrom(const ProtoEvent& from) {
  GOOGLE_DCHECK(&from != this);
  if (from._has_bits_[0 / 32] & (0xffu << (0 % 32))) {
    if (from.has_time()) {
      set_time(from.time());
    }
    if (from.has_event()) {
      set_event(from.event());
    }
    if (from.has_message()) {
      set_has_message();
      message_.AssignWithDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), from.message_);
    }
    if (from.has_what1()) {
      set_what1(from.what1());
    }
    if (from.has_what2()) {
      set_what2(from.what2());
    }
    if (from.has_json()) {
      set_has_json();
      json_.AssignWithDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), from.json_);
    }
  }
  if (from._internal_metadata_.have_unknown_fields()) {
    ::google::protobuf::UnknownFieldSet::MergeToInternalMetdata(
      from.unknown_fields(), &_internal_metadata_);
  }
}

void ProtoEvent::CopyFrom(const ::google::protobuf::Message& from) {
// @@protoc_insertion_point(generalized_copy_from_start:ProtoEvent)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

void ProtoEvent::CopyFrom(const ProtoEvent& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:ProtoEvent)
  if (&from == this) return;
  Clear();
  UnsafeMergeFrom(from);
}

bool ProtoEvent::IsInitialized() const {

  return true;
}

void ProtoEvent::Swap(ProtoEvent* other) {
  if (other == this) return;
  InternalSwap(other);
}
void ProtoEvent::InternalSwap(ProtoEvent* other) {
  std::swap(time_, other->time_);
  std::swap(event_, other->event_);
  message_.Swap(&other->message_);
  std::swap(what1_, other->what1_);
  std::swap(what2_, other->what2_);
  json_.Swap(&other->json_);
  std::swap(_has_bits_[0], other->_has_bits_[0]);
  _internal_metadata_.Swap(&other->_internal_metadata_);
  std::swap(_cached_size_, other->_cached_size_);
}

::google::protobuf::Metadata ProtoEvent::GetMetadata() const {
  protobuf_AssignDescriptorsOnce();
  ::google::protobuf::Metadata metadata;
  metadata.descriptor = ProtoEvent_descriptor_;
  metadata.reflection = ProtoEvent_reflection_;
  return metadata;
}

#if PROTOBUF_INLINE_NOT_IN_HEADERS
// ProtoEvent

// optional int64 time = 1;
bool ProtoEvent::has_time() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
void ProtoEvent::set_has_time() {
  _has_bits_[0] |= 0x00000001u;
}
void ProtoEvent::clear_has_time() {
  _has_bits_[0] &= ~0x00000001u;
}
void ProtoEvent::clear_time() {
  time_ = GOOGLE_LONGLONG(0);
  clear_has_time();
}
::google::protobuf::int64 ProtoEvent::time() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.time)
  return time_;
}
void ProtoEvent::set_time(::google::protobuf::int64 value) {
  set_has_time();
  time_ = value;
  // @@protoc_insertion_point(field_set:ProtoEvent.time)
}

// optional int64 event = 2;
bool ProtoEvent::has_event() const {
  return (_has_bits_[0] & 0x00000002u) != 0;
}
void ProtoEvent::set_has_event() {
  _has_bits_[0] |= 0x00000002u;
}
void ProtoEvent::clear_has_event() {
  _has_bits_[0] &= ~0x00000002u;
}
void ProtoEvent::clear_event() {
  event_ = GOOGLE_LONGLONG(0);
  clear_has_event();
}
::google::protobuf::int64 ProtoEvent::event() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.event)
  return event_;
}
void ProtoEvent::set_event(::google::protobuf::int64 value) {
  set_has_event();
  event_ = value;
  // @@protoc_insertion_point(field_set:ProtoEvent.event)
}

// optional string message = 3;
bool ProtoEvent::has_message() const {
  return (_has_bits_[0] & 0x00000004u) != 0;
}
void ProtoEvent::set_has_message() {
  _has_bits_[0] |= 0x00000004u;
}
void ProtoEvent::clear_has_message() {
  _has_bits_[0] &= ~0x00000004u;
}
void ProtoEvent::clear_message() {
  message_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_message();
}
const ::std::string& ProtoEvent::message() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.message)
  return message_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
void ProtoEvent::set_message(const ::std::string& value) {
  set_has_message();
  message_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:ProtoEvent.message)
}
void ProtoEvent::set_message(const char* value) {
  set_has_message();
  message_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:ProtoEvent.message)
}
void ProtoEvent::set_message(const char* value, size_t size) {
  set_has_message();
  message_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:ProtoEvent.message)
}
::std::string* ProtoEvent::mutable_message() {
  set_has_message();
  // @@protoc_insertion_point(field_mutable:ProtoEvent.message)
  return message_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
::std::string* ProtoEvent::release_message() {
  // @@protoc_insertion_point(field_release:ProtoEvent.message)
  clear_has_message();
  return message_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
void ProtoEvent::set_allocated_message(::std::string* message) {
  if (message != NULL) {
    set_has_message();
  } else {
    clear_has_message();
  }
  message_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), message);
  // @@protoc_insertion_point(field_set_allocated:ProtoEvent.message)
}

// optional int64 what1 = 4;
bool ProtoEvent::has_what1() const {
  return (_has_bits_[0] & 0x00000008u) != 0;
}
void ProtoEvent::set_has_what1() {
  _has_bits_[0] |= 0x00000008u;
}
void ProtoEvent::clear_has_what1() {
  _has_bits_[0] &= ~0x00000008u;
}
void ProtoEvent::clear_what1() {
  what1_ = GOOGLE_LONGLONG(0);
  clear_has_what1();
}
::google::protobuf::int64 ProtoEvent::what1() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.what1)
  return what1_;
}
void ProtoEvent::set_what1(::google::protobuf::int64 value) {
  set_has_what1();
  what1_ = value;
  // @@protoc_insertion_point(field_set:ProtoEvent.what1)
}

// optional int64 what2 = 5;
bool ProtoEvent::has_what2() const {
  return (_has_bits_[0] & 0x00000010u) != 0;
}
void ProtoEvent::set_has_what2() {
  _has_bits_[0] |= 0x00000010u;
}
void ProtoEvent::clear_has_what2() {
  _has_bits_[0] &= ~0x00000010u;
}
void ProtoEvent::clear_what2() {
  what2_ = GOOGLE_LONGLONG(0);
  clear_has_what2();
}
::google::protobuf::int64 ProtoEvent::what2() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.what2)
  return what2_;
}
void ProtoEvent::set_what2(::google::protobuf::int64 value) {
  set_has_what2();
  what2_ = value;
  // @@protoc_insertion_point(field_set:ProtoEvent.what2)
}

// optional string json = 6;
bool ProtoEvent::has_json() const {
  return (_has_bits_[0] & 0x00000020u) != 0;
}
void ProtoEvent::set_has_json() {
  _has_bits_[0] |= 0x00000020u;
}
void ProtoEvent::clear_has_json() {
  _has_bits_[0] &= ~0x00000020u;
}
void ProtoEvent::clear_json() {
  json_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_json();
}
const ::std::string& ProtoEvent::json() const {
  // @@protoc_insertion_point(field_get:ProtoEvent.json)
  return json_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
void ProtoEvent::set_json(const ::std::string& value) {
  set_has_json();
  json_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:ProtoEvent.json)
}
void ProtoEvent::set_json(const char* value) {
  set_has_json();
  json_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:ProtoEvent.json)
}
void ProtoEvent::set_json(const char* value, size_t size) {
  set_has_json();
  json_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:ProtoEvent.json)
}
::std::string* ProtoEvent::mutable_json() {
  set_has_json();
  // @@protoc_insertion_point(field_mutable:ProtoEvent.json)
  return json_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
::std::string* ProtoEvent::release_json() {
  // @@protoc_insertion_point(field_release:ProtoEvent.json)
  clear_has_json();
  return json_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
void ProtoEvent::set_allocated_json(::std::string* json) {
  if (json != NULL) {
    set_has_json();
  } else {
    clear_has_json();
  }
  json_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), json);
  // @@protoc_insertion_point(field_set_allocated:ProtoEvent.json)
}

inline const ProtoEvent* ProtoEvent::internal_default_instance() {
  return &ProtoEvent_default_instance_.get();
}
#endif  // PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

// @@protoc_insertion_point(global_scope)
