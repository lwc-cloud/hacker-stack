import dns.resolver
import json

resolver = dns.resolver.Resolver()
# 定义一个函数来查询DNS记录
def query_dns_records(domain, record_type):
    try:
        records = resolver.resolve(domain, record_type)
        return [r.to_text() for r in records]
    except (dns.resolver.NoAnswer, dns.resolver.NXDOMAIN, dns.resolver.NoNameservers):
        return []
    
def get_dns(domain):
    dns_records = {
        'A': query_dns_records(domain, 'A'),
        'MX': query_dns_records(domain, 'MX'),
        'TXT': query_dns_records(domain, 'TXT'),
        'NS': query_dns_records(domain, 'NS'),
        'CNAME': query_dns_records(domain, 'CNAME'),
        'SOA': query_dns_records(domain, 'SOA')
    }
    dns_records_json = json.dumps(dns_records, indent=4)
    return dns_records_json